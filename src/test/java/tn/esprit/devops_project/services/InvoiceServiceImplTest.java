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
import tn.esprit.devops_project.entities.Invoice;
import tn.esprit.devops_project.entities.Operator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
class InvoiceServiceImplTest {

    @Autowired
    private InvoiceServiceImpl invoiceService;
    @Autowired
    private OperatorServiceImpl operatorService;


    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void cancelInvoice() {
        this.invoiceService.cancelInvoice(1L);
        assertEquals(this.invoiceService.retrieveInvoice(1L).getArchived(), true);
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveAllInvoices() {
        final List<Invoice> allInvoices = this.invoiceService.retrieveAllInvoices();
        assertEquals(allInvoices.size(), 1);

    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void retrieveInvoice() {
        final Invoice invoice = this.invoiceService.retrieveInvoice(1L);
        assertEquals(invoice.getAmountInvoice(), 100f);
    }

    /*  @Test
      @DatabaseSetup({"/data-set/invoice-data.xml","/data-set/supplier-data.xml"})
      void getInvoicesBySupplier() {
          final Invoice invoice = new Invoice();
          invoice.setSupplier(this.supplierService.retrieveSupplier(1L));
          assertEquals(this.invoiceService.getInvoicesBySupplier(1L).size(), 1);
      }
  */
    @Test
    @DatabaseSetup({"/data-set/invoice-data.xml", "/data-set/operator-data.xml"})
    void assignOperatorToInvoice() {
        this.invoiceService.assignOperatorToInvoice(1L, 1L);
        assertEquals(this.operatorService.retrieveOperator(1L).getInvoices().size(), 1);
    }

    @Test
    @DatabaseSetup("/data-set/invoice-data.xml")
    void getTotalAmountInvoiceBetweenDates()  {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        float amount= 0;
        try {
            amount = this.invoiceService.getTotalAmountInvoiceBetweenDates(dateFormat.parse("2019-08-26"),dateFormat.parse("2020-12-26"));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        assertEquals(amount, 100);
    }

}