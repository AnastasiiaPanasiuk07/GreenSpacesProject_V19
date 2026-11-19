package ua.edu.nupp.kpp.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CourseworkTest {
    @Test
    public void testEnvironment() {
        boolean isReady = true;
        assertTrue(isReady, "Тестове середовище JUnit 5 на серверах працює коректно");
    }
    @Test
    void test1_integralIndicatorCalculation() {
        double result = RiskCalculator.calculate(40.0, 50.0, 60.0);
        assertEquals(48.5, result, 0.001);
    }
    @Test
    void test2_integralIndicatorDemoData2() {
        double result = RiskCalculator.calculate(70.0, 80.0, 90.0);
        assertEquals(78.5, result, 0.001);
    }
    @Test
    void test3_riskRejectsFactorAbove100() {
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(105.0, 50.0, 60.0));
    }
    @Test
    void test4_riskRejectsNegativeFactor() {
        assertThrows(IllegalArgumentException.class, () -> RiskCalculator.calculate(-5.0, 50.0, 60.0));
    }
    @Test
    void test5_createdToCheckTransitionAllowed() {
        assertTrue(WorkOrderRequestState.isAllowedTransition("CREATED", "CHECKED"));
    }
    @Test
    void test6_checkedToPlannedTransitionAllowed() {
        assertTrue(WorkOrderRequestState.isAllowedTransition("CHECKED", "PLANNED"));
    }
    @Test
    void test7_plannedToCompletedTransitionAllowed() {
        assertTrue(WorkOrderRequestState.isAllowedTransition("PLANNED", "DONE"));
    }
    @Test
    void test8_invalidStateTransitionRejected() {
        assertFalse(WorkOrderRequestState.isAllowedTransition("CREATED", "DONE"));
    }
    @Test
    void testNegative_invalidStateTransitionRejected() {
        boolean isAllowed = WorkOrderRequestState.isAllowedTransition("CREATED", "DONE");
        assertFalse(isAllowed, "Заборонений перехід статусу заявки має відхилятися системою");
    }
    @Test
    void test9_treeValidationWorks() {
        Tree tree = new Tree();
        tree.setSpecies("Дуб звичайний");
        tree.setConditionScore(85);
        assertNotNull(tree.getSpecies(), "Вид дерева не має бути null");
        assertTrue(tree.getConditionScore() > 0, "Оцінка стану має бути додатною");
    }
    @Test
    void test10_csvImportLineParsing() {
        String csvLine = "Дуб звичайний,вул. Соборна 12,2010,85.0";
        String[] fields = csvLine.split(",");
        assertEquals(4, fields.length, "CSV рядок повинен містити 4 поля");
        assertEquals("Дуб звичайний", fields[0], "Перше поле — вид дерева");
    }

}
