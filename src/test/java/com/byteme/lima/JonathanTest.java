package com.byteme.lima;

import com.byteme.lima.config.ApplicationConfiguration;
import com.byteme.lima.domain.Project;
import com.byteme.lima.domain.Task;
import com.byteme.lima.domain.Team;
import com.byteme.lima.domain.User;
import com.byteme.lima.exception.IllegalStateException;
import com.byteme.lima.service.EventService;
import com.byteme.lima.service.ProjectService;
import com.byteme.lima.service.TaskService;
import com.byteme.lima.service.TeamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringApplicationConfiguration(ApplicationConfiguration.class)
@WebIntegrationTest
public class JonathanTest {

    @Autowired
    public EventService eventService;

    @Autowired
    public TaskService taskService;

    @Autowired
    public TeamService teamService;

    @Autowired
    public ProjectService projectService;

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    @PostConstruct
    public void postConstruct() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .alwaysExpect(status().isOk())
                .build();
        System.out.println(); //this is to add extra linebreak
    }

    @Test
    public void test1() throws JsonProcessingException {
        for (Project project: this.projectService.findAll()) {
//            project.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//            this.projectService.save(project);
//            System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(project));

            for (String taskId: project.getTaskIds()) {
                Task task = this.taskService.findById(taskId);
//                task.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
//                task.setName("Title for " + task.code);
//                this.taskService.save(task);
//                System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(task));
            }
        }
    }

    @Test
    public void test2() throws JsonProcessingException, IllegalStateException {
        for (Project project: this.projectService.findAll()) {
            project = this.projectService.fetchTasks(project);
            for (Task task: project.tasks) {
                this.eventService.add(project, task);

                task = this.taskService.fetchAssignee(task);
                this.eventService.assign(task, task.assignee);
            }

            Team team = this.teamService.findById(project.teamId);
            this.eventService.assign(project, team);

            team = this.teamService.fetchMembers(team);
            for (User user: team.members) {
                this.eventService.assign(user, team);
            }


        }
    }

    @Test
    public void test3() {
        for (Project project: this.projectService.findAll()) {
            Team team = this.teamService.findById(project.teamId);
            team = this.teamService.fetchMembers(team);

            int i = 1;
            for (User user: team.members) {

                i++;
            }
        }
    }

    @Test
    public void test4() throws Exception {
        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            this.mockMvc.perform(get("/rest/projects/56c8dacbd6e487680bead2cf"));
            System.out.println("time taken: " + (System.currentTimeMillis() - start) + " ms");
        }
    }
}
