import br.com.fabricio.acceleradora_agil.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;

import br.com.fabricio.acceleradora_agil.singleton.AppointmentManager;

class AppointmentManagerTest {
    private Patient[] patients;


    @BeforeEach
    void beforeEach() {
        this.patients = new Patient[]{new Patient("JÔE", "51990021234"),
                new Patient("MELANIE", "51990013221"),
                new Patient("GOAL", "51990076543")};

    }

    @Test
    void addPatientTest() {
        AppointmentManager ap = AppointmentManager.ISTANCE;

        for (int i = 0; i < this.patients.length; i++) {
            ap.addPatient(this.patients[i]);
        }

        // SE TODOS PACIENTES FORAM CADASTRADOS
        Assertions.assertEquals(ap.getPatients().size(), this.patients.length);

        // SE HÁ UMA EXCEÇÂO AO TENTAR ADICIONAR UM PACIENTE EXISTENTE FOR LANÇADA
        Patient patient = new Patient("JOREL", "51990021234");
        Assertions.assertThrows(IllegalArgumentException.class, () -> ap.addPatient(patient));
    }
}