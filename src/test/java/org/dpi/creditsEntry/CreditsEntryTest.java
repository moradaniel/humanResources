package org.dpi.creditsEntry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.creditsPeriod.CreditsPeriodDao;
import org.dpi.creditsPeriod.CreditsPeriodImpl;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.creditsPeriod.CreditsPeriodServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

public class CreditsEntryTest {
    
    private CreditsPeriod CLOSED_PERIOD_OLDER_THAN_1YEAR = new CreditsPeriodImpl();
    private CreditsPeriod CLOSED_PERIOD_NOT_OLDER_THAN_1YEAR = new CreditsPeriodImpl();
    CreditsEntryService creditsEntryService;
    CreditsPeriodService creditsPeriodService;
    

    @Before
    public void initialize() {
    
        creditsEntryService = new CreditsEntryServiceImpl();
        creditsPeriodService = new TestableCreditsPeriodService(null);
        
        
        CLOSED_PERIOD_OLDER_THAN_1YEAR.setStatus(CreditsPeriod.Status.Closed);
        CLOSED_PERIOD_OLDER_THAN_1YEAR.setName("2012");
        
        CLOSED_PERIOD_NOT_OLDER_THAN_1YEAR.setStatus(CreditsPeriod.Status.Closed);
        CLOSED_PERIOD_NOT_OLDER_THAN_1YEAR.setName("2013");

    }
    
    @Test
    public void creditsEntryStatus_should_not_be_changed_for_closed_period_and_period_greater_than_1_year() {

        CreditsEntry aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                                     .withCreditsPeriod(CLOSED_PERIOD_OLDER_THAN_1YEAR)
                                     .build();
        
        
        assertFalse(aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));

    }
    
    
    @Test
    public void creditsEntryStatus_should_not_be_changed_for_closed_period_and_is_BajaAgente() {

        
        CreditsEntry aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                .withCreditsPeriod(CLOSED_PERIOD_NOT_OLDER_THAN_1YEAR)
                .withCreditsEntryType(CreditsEntryType.BajaAgente)
                .build();
        
        assertFalse(aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));
        
    }
    
    @Test
    public void creditsEntryStatus_should_not_be_changed_for_closed_period_and_hasSubsequentCreditsEntries() {

        
        CreditsEntry aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                .withCreditsPeriod(CLOSED_PERIOD_NOT_OLDER_THAN_1YEAR)
                .withCreditsEntryType(CreditsEntryType.AscensoAgente)
                .withSubsequentCreditsEntries(new TestableCreditsEntry())
                .build();
        
        assertFalse(aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));
        
    }
    
    @Test
    public void creditsEntryStatus_can_be_changed_for_closed_period_and_is_AscensoAgente() {

        CreditsEntry creditsEntry = new TestableCreditsEntry();
        creditsEntry.setCreditsEntryType(CreditsEntryType.AscensoAgente);
        
        creditsEntry.setCreditsPeriod(CLOSED_PERIOD_NOT_OLDER_THAN_1YEAR);
        
        assertTrue("creditsEntryStatus_can_be_changed_for_closed_period_and_is_AscensoAgente",
                creditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));
        
    }

 
    @Test
    public void creditsEntryStatus_can_be_changed_for_active_period() {

        CreditsEntry aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                .withCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod())
                .withCreditsEntryType(CreditsEntryType.AscensoAgente)
                .build();
        
        assertTrue("creditsEntryStatus_can_be_changed_for_active_period_and_is_AscensoAgente",
                aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));
        
        
        aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                .withCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod())
                .withCreditsEntryType(CreditsEntryType.BajaAgente)
                .build();
        
        assertTrue("creditsEntryStatus_can_be_changed_for_active_period_and_is_BajaAgente",
                aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));

    }
    
    
    @Test
    public void creditsEntryStatus_should_not_be_changed_for_active_period_and_CargaInicialAgenteExistente() {

        CreditsEntry aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                .withCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod())
                .withCreditsEntryType(CreditsEntryType.CargaInicialAgenteExistente)
                .build();
        
        assertFalse("creditsEntryStatus_should_not_be_changed_for_active_period_and_CargaInicialAgenteExistente",
                aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));

    }
    
    
    @Test
    public void creditsEntryStatus_should_not_be_changed_for_active_period_and_IngresoWithoutCredits() {

        CreditsEntry aCreditsEntry = new CreditsEntryBuilder().aCreditsEntry()
                .withCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod())
                .withCreditsEntryType(CreditsEntryType.IngresoAgente)
                .withNumberOfCredits(0)
                .build();
        
        assertFalse("creditsEntryStatus_should_not_be_changed_for_active_period_and_IngresoWithoutCredits",
                aCreditsEntry.canStatusBeChanged(creditsEntryService, creditsPeriodService));

    }
    
    
    @Test
    public void creditsEntryStatus_can_be_changed_for_active_period_and_is_BajaAgente() {

         
    }


    
    private class TestableCreditsPeriodService extends CreditsPeriodServiceImpl{
        
        public TestableCreditsPeriodService(CreditsPeriodDao creditsPeriodDao) {
            super(creditsPeriodDao);
            // TODO Auto-generated constructor stub
        }

        public CreditsPeriod getCurrentCreditsPeriod(){
            CreditsPeriod currentCreditsPeriod = new CreditsPeriodImpl();
            currentCreditsPeriod.setName("2014");
            currentCreditsPeriod.setStatus(Status.Active);
            return currentCreditsPeriod;
        }

    }

    
    private class TestableCreditsEntry extends CreditsEntryImpl{
        
        @Override
        public boolean hasSubsequentEntries(CreditsEntryService creditsEntryService) {
            return !CollectionUtils.isEmpty(this.getSubsequentCreditsEntries(creditsEntryService));
        }
        
        @Override
        public List<CreditsEntry> getSubsequentCreditsEntries(CreditsEntryService creditsEntryService){
            return this.subsequentCreditEntries;
        }
    }
    
    private class CreditsEntryBuilder{
        
        private CreditsPeriod creditsPeriod;
        private CreditsEntryType creditsEntryType;
        private CreditsEntry[] subsequentCreditsEntries = {};
        private int numberOfCredits;
        
        CreditsEntryBuilder aCreditsEntry() {
            return new CreditsEntryBuilder();
        }
        
        public CreditsEntryBuilder withNumberOfCredits(int numberOfCredits) {
            this.numberOfCredits = numberOfCredits;
            return this;
        }

        public CreditsEntryBuilder withSubsequentCreditsEntries(TestableCreditsEntry ... subsequentCreditsEntries) {
            this.subsequentCreditsEntries  = subsequentCreditsEntries;
            return this;
        }

        public CreditsEntryBuilder withCreditsEntryType(
                CreditsEntryType creditsEntryType) {
            this.creditsEntryType = creditsEntryType;
            return this;
        }

        public CreditsEntryBuilder withCreditsPeriod(
                CreditsPeriod creditsPeriod) {
            this.creditsPeriod = creditsPeriod;
            return this;
        }
        
        

        public CreditsEntry build() {
            CreditsEntry creditsEntry =  new TestableCreditsEntry();
            creditsEntry.setCreditsPeriod(this.creditsPeriod);
            creditsEntry.setCreditsEntryType(this.creditsEntryType);
            creditsEntry.setSubsequentCreditsEntries(Arrays.asList(this.subsequentCreditsEntries));
            creditsEntry.setNumberOfCredits(this.numberOfCredits);
            return creditsEntry;
        }
    }

}
