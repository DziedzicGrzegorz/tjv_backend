package cz.cvut.tjv_backend.service;

import cz.cvut.tjv_backend.entity.Group;
import cz.cvut.tjv_backend.entity.User;
import cz.cvut.tjv_backend.repository.GroupRepository;
import cz.cvut.tjv_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GroupService {

    private GroupRepository groupRepository;

    private UserRepository userRepository;

    public Group createGroup(String name, String description, Set<String> usersToAdd, UUID founderId) {
        Group group = new Group();
        group.setName(name);
        group.setDescription(description);
        group.setFounder(userRepository.findById(founderId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found")));
        Set<User> users = usersToAdd.stream()
                .map(userRepository::findByEmail)
                .map(optionalUser -> optionalUser.orElseThrow(() -> new IllegalArgumentException("User not found for email: " + optionalUser)))
                .collect(Collectors.toSet());
        group.setUsers(users);
        return groupRepository.save(group);
    }

    public Group addUsersToGroup(UUID groupId, UUID ownerId, Set<UUID> userIds) {
        return groupRepository.findById(groupId).map(group -> {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
            if (!group.getFounder().equals(owner) && !group.getAdmins().contains(owner)) {
                throw new IllegalArgumentException("Only group owners or admins can add users to the group");
            }
            Set<User> users = new HashSet<>(userRepository.findAllById(userIds));
            group.getUsers().addAll(users);
            return groupRepository.save(group);
        }).orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }

    public Group removeUsersFromGroup(UUID groupId, UUID ownerId, Set<UUID> userIds) {
        return groupRepository.findById(groupId).map(group -> {
            User owner = userRepository.findById(ownerId)
                    .orElseThrow(() -> new IllegalArgumentException("Owner not found"));
            if (!group.getAdmins().contains(owner)) {
                throw new IllegalArgumentException("Only group admins can remove users from the group");
            }
            Set<User> users = new HashSet<>(userRepository.findAllById(userIds));
            for (User user : users) {
                if (group.getFounder().equals(user)) {
                    throw new IllegalArgumentException("Admins cannot remove group owners");
                }
            }
            group.getUsers().removeAll(users);
            return groupRepository.save(group);
        }).orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }

    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public Optional<Group> getGroupById(UUID groupId) {
        return groupRepository.findById(groupId);
    }

    public Group updateGroup(UUID groupId, Group updatedGroup) {
        return groupRepository.findById(groupId).map(group -> {
            group.setName(updatedGroup.getName());
            group.setDescription(updatedGroup.getDescription());
            return groupRepository.save(group);
        }).orElseThrow(() -> new IllegalArgumentException("Group not found"));
    }

    public void deleteGroup(UUID groupId) {
        groupRepository.deleteById(groupId);
    }

    public List<Group> getAllGroupsByUser(UUID userId) {
        return groupRepository.findByUsersId(userId);
    }

}
