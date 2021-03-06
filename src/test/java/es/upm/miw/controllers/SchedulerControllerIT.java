package es.upm.miw.controllers;

import es.upm.miw.documents.core.Scheduler;
import es.upm.miw.dtos.SchedulerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class SchedulerControllerIT {

    @Autowired
    private SchedulerController schedulerController;

    private SchedulerDto schedulerDto;

    @Before
    public void before(){
        this.schedulerDto = new SchedulerDto();
        Date now = new Date();
        this.schedulerDto.setDateTime(now);
        this.schedulerDto.setTitle("Title");
        this.schedulerDto.setDescription("Description");
        schedulerController.create(this.schedulerDto);
    }

    //@After
    //public void after(){
    //    this.schedulerController.deleteAll();
    //}

    @Test
    public void testCreateScheduler(){
        SchedulerDto schedulerDtoTest = schedulerController.create(this.schedulerDto);
        assert(schedulerDtoTest != null);
    }

    @Test
    public void testReadAllScheduler(){
        schedulerController.create(this.schedulerDto);

        Date pastDate = new GregorianCalendar(2018, 2, 12, 19,30).getTime();

        Scheduler schedulerPast = new Scheduler(pastDate, "Title2", "Descrition2");
        schedulerController.create(new SchedulerDto(schedulerPast));

        List<SchedulerDto> schedulerList = schedulerController.readAll();
        assertTrue(schedulerList != null);
        assertEquals(5, schedulerList.size());
        assertTrue(schedulerList.get(0).getDateTime().before(schedulerList.get(1).getDateTime()));
    }

    @Test
    public void testReadScheduler(){
        SchedulerDto schedulerDTOTest = schedulerController.create(this.schedulerDto);
        assert(schedulerDTOTest != null);

        Optional<SchedulerDto> optional = schedulerController.read(schedulerDTOTest.getId());
        SchedulerDto schedulerDtoFromDDBB = optional.get();
        assertTrue(optional.isPresent());

        assertTrue( schedulerDtoFromDDBB.getId().equals(schedulerDTOTest.getId()));
    }

    @Test
    public void testDeleteScheduler(){
        SchedulerDto schedulerDTOTest = schedulerController.create(this.schedulerDto);
        assert(schedulerDTOTest != null);

        schedulerController.delete(schedulerDTOTest.getId());

        Optional<SchedulerDto> optional = schedulerController.read(schedulerDTOTest.getId());
        assert(!optional.isPresent());
    }
}
