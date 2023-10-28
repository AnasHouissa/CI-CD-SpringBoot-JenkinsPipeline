package tn.esprit.devops_project.services;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.devops_project.entities.Operator;
import tn.esprit.devops_project.entities.Stock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
class OperatorServiceImplTest {

    @Autowired
    private OperatorServiceImpl operatorService;

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void addOperator() {
        final Operator op = new Operator();
        op.setFname("Fname 2");
        this.operatorService.addOperator(op);
        assertEquals(this.operatorService.retrieveAllOperators().size(),2);
        assertEquals(this.operatorService.retrieveOperator(2L).getFname(),"Fname 2");
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveStock() {
        final Operator op = this.operatorService.retrieveOperator(1L);
        assertEquals("fname 1", op.getFname());
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void retrieveAllStock() {
        final List<Operator> allOperator = this.operatorService.retrieveAllOperators();
        assertEquals(allOperator.size(), 1);

    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void deleteOperator() {
        this.operatorService.deleteOperator(1L);
        assertEquals(this.operatorService.retrieveAllOperators().size(), 0);
    }

    @Test
    @DatabaseSetup("/data-set/operator-data.xml")
    void updateOperator() {
        Operator op =this.operatorService.retrieveOperator(1L);
        op.setLname("Lname 1 updated");
        this.operatorService.updateOperator(op);
        assertEquals(this.operatorService.retrieveOperator(1L).getLname(), "Lname 1 updated");
    }
}