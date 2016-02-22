package com.byteme.lima.service;

import com.byteme.lima.domain.*;
import com.byteme.lima.exception.IllegalStateException;
import com.byteme.lima.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BootstrapService extends AbstractService {

    @Autowired
    public ProjectService projectService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public UserService userService;

    @Autowired
    public EventService eventService;

    public AtomicInteger integer = new AtomicInteger();

    public synchronized Integer next() {
        return this.integer.incrementAndGet();
    }

    public synchronized ObjectId toObjectId(char prefix, Integer integer) {
        return new ObjectId(String.format("%s%023d", prefix, integer));
    }

    public synchronized String toStringId(char prefix, Integer integer) {
        return String.format("%s%023d", prefix, integer);
    }

    public void cleanup() {
        this.projectService.removeAll();
        this.teamService.removeAll();
        this.taskService.removeAll();
        this.userService.removeAll();
        this.eventService.removeAll();
    }

    public void bootstrap() throws IllegalStateException {
        this.cleanup();
        this.admin();

        for (int i = 0; i < 3; i++) {
            Project project = new Project();
            Integer _project = this.integer.incrementAndGet();
            project.setCode("Proj-" + _project);
            project.setName("Name for Project with code " + project.getCode());
            project.setDescription("This is a long description for Project with code: [" + project.getCode() + "] and name: [" + project.getName() + "]. This is extra sentence to make the description longer. This is another extra sentence to make the description longer still.");
            project.setStart(new Date(System.currentTimeMillis() + (2L * Constants.Dates.ONE_WEEK)));
            project.setEnd(new Date(System.currentTimeMillis() + (5L * Constants.Dates.ONE_WEEK)));
            project.setStatus(Project.Status.BACKLOG);
            project.setTaskIds(new ArrayList<>());

            Team team = new Team();
            Integer _team = this.integer.incrementAndGet();
            team.setCode("Team-" + _team);
            team.setName("Name for Team with code " + team.getCode());
            team.setMemberIds(new ArrayList<>());
            for (int ii = 0; ii < 4; ii++) {
                //create user
                User user = new User();
                Integer _user = this.integer.incrementAndGet();
                user.setCode("User-" + _user);
                user.setName("Name for User with code " + user.getCode());
                user.setEmail("user." + _user + "@lima.com");
                user.setAvatar(Constants.Avatar.USER);
                user.setType(User.Type.USER);

                user = this.userService.save(user);
                team.getMemberIds().add(user.getId());
                team = this.teamService.save(team);

                //create task
                Task task = new Task();
                Integer _task = this.integer.incrementAndGet();
                task.setCode("Task-" + _task);
                task.setName("Tas Title for Title with code " + task.getCode());
                task.setDescription("This is a long description for Task with code: [" + task.getCode() + "] and name: [" + task.getName() + "]. This is extra sentence to make the description longer. This is another extra sentence to make the description longer still. Let's add another sentence to make it longer still.");
                task.setStart(project.getStart());
                task.setEnd(project.getEnd());
                task.setStatus(Task.Status.BACKLOG);
                task.setAssigneeId(user.getId());

                task = this.taskService.save(task);
                project.getTaskIds().add(task.getId());

                project = this.projectService.save(project);
                //history
                eventService.assign(user, team);
                eventService.add(project, task);
                eventService.assign(task, user);
                eventService.assign(project, team);
            }

            team = this.teamService.save(team);

            //set team
            project.setTeamId(team.getId());
            project = this.projectService.save(project);
        }
    }

    public void admin() {
        User admin = new User();
        admin.code = "admin";
        admin.name = "Administrator";
        admin.email = "admin@lima.com";
        admin.avatar = Constants.Avatar.ADMIN;
        admin.type = User.Type.ADMIN;

        this.userService.save(admin);
    }
}
