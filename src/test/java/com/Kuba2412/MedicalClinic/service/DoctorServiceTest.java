package com.Kuba2412.MedicalClinic.service;

import com.Kuba2412.MedicalClinic.model.Doctor;
import com.Kuba2412.MedicalClinic.model.Institution;
import com.Kuba2412.MedicalClinic.model.dto.DoctorDTO;
import com.Kuba2412.MedicalClinic.model.mapper.DoctorMapper;
import com.Kuba2412.MedicalClinic.model.mapper.SimpleDoctorDTO;
import com.Kuba2412.MedicalClinic.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceTest {

    private DoctorService doctorService;
    private DoctorRepository doctorRepository;
    private DoctorMapper doctorMapper;

    @BeforeEach
    void setup() {
        doctorRepository = Mockito.mock(DoctorRepository.class);
        doctorMapper = Mockito.mock(DoctorMapper.class);
        doctorService = new DoctorService(doctorRepository, doctorMapper);
    }

    @Test
    void createDoctor_ValidDoctorDTO_DoctorSaved() {
        // given
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setFirstName("Kuba");
        doctorDTO.setLastName("Nowak");
        doctorDTO.setSpecialization("Specjalizacja");

        Doctor doctor = new Doctor();
        doctor.setFirstName(doctorDTO.getFirstName());
        doctor.setLastName(doctorDTO.getLastName());
        doctor.setSpecialization(doctorDTO.getSpecialization());
        when(doctorMapper.toDoctor(doctorDTO)).thenReturn(doctor);

        // when
        doctorService.createDoctor(doctorDTO);

        // then
        verify(doctorMapper, times(1)).toDoctor(doctorDTO);
        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void createDoctor_NullDoctorDTO_ThrowsException() {
        // given
        DoctorDTO doctorDTO = null;

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> doctorService.createDoctor(doctorDTO));
        assertEquals("Doctor can't be null.", exception.getMessage());
        verify(doctorMapper, never()).toDoctor(any(DoctorDTO.class));
        verify(doctorRepository, never()).save(any(Doctor.class));
    }

    @Test
    void getAllDoctors_DoctorsExist_DoctorsReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Doctor doctor1 = new Doctor();
        doctor1.setId(1L);
        doctor1.setFirstName("Kuba");
        doctor1.setLastName("Ppp");
        doctor1.setSpecialization("Kardiologia");

        Doctor doctor2 = new Doctor();
        doctor2.setId(2L);
        doctor2.setFirstName("Anna");
        doctor2.setLastName("Sss");
        doctor2.setSpecialization("Neurologia");

        List<Doctor> doctors = Arrays.asList(doctor1, doctor2);
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);

        // when
        List<SimpleDoctorDTO> result = doctorService.getAllDoctors(pageable);

        // then
        assertNotNull(result);
        assertEquals(doctors.size(), result.size());

        assertEquals(doctor1.getFirstName(), result.get(0).getFirstName());
        assertEquals(doctor1.getLastName(), result.get(0).getLastName());
        assertEquals(doctor1.getSpecialization(), result.get(0).getSpecialization());

        assertEquals(doctor2.getFirstName(), result.get(1).getFirstName());
        assertEquals(doctor2.getLastName(), result.get(1).getLastName());
        assertEquals(doctor2.getSpecialization(), result.get(1).getSpecialization());

        verify(doctorMapper, times(doctors.size())).toSimpleDoctorDTO(any(Doctor.class));
    }

    @Test
    void getAllDoctors_NoDoctorsExist_EmptyListReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> doctorPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);

        // when
        List<SimpleDoctorDTO> result = doctorService.getAllDoctors(pageable);

        // then
        assertNotNull(result);
        assertEquals(0, result.size());

        verify(doctorMapper, never()).toSimpleDoctorDTO(any(Doctor.class));
    }

    @Test
    void getAllSimpleDoctors_DoctorsExist_DoctorsReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<Doctor> doctors = Arrays.asList(new Doctor(), new Doctor());
        Page<Doctor> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);

        // when
        List<SimpleDoctorDTO> result = doctorService.getAllSimpleDoctors(pageable);

        // then
        assertNotNull(result);
        assertEquals(doctors.size(), result.size());
        verify(doctorMapper, times(doctors.size())).toSimpleDoctorDTO(any(Doctor.class));
    }

    @Test
    void getAllSimpleDoctors_NoDoctorsExist_EmptyListReturned() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Doctor> doctorPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
        when(doctorRepository.findAll(pageable)).thenReturn(doctorPage);

        // when
        List<SimpleDoctorDTO> result = doctorService.getAllSimpleDoctors(pageable);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(doctorMapper, never()).toSimpleDoctorDTO(any(Doctor.class));
    }

    @Test
    void getAssignedInstitutionsForDoctor_DoctorExists_InstitutionsReturned() {
        // given
        Long doctorId = 1L;
        Doctor doctor = new Doctor();

        Institution institution1 = new Institution();
        institution1.setId(1L);
        institution1.setName("Szpital 1");
        institution1.setCity("Wrocław");
        institution1.setPostalCode("01-123");
        institution1.setStreet("Górna");
        institution1.setBuildingNumber("10");

        Institution institution2 = new Institution();
        institution2.setId(2L);
        institution2.setName("Szpital 2");
        institution2.setCity("Wrocław");
        institution2.setPostalCode("01-123");
        institution2.setStreet("Mała");
        institution2.setBuildingNumber("42");

        List<Institution> institutions = Arrays.asList(institution1, institution2);
        doctor.setInstitutions(institutions);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        // when
        List<Institution> result = doctorService.getAssignedInstitutionsForDoctor(doctorId);

        // then
        assertNotNull(result);
        assertEquals(institutions.size(), result.size());

        Institution resultInstitution1 = result.get(0);
        assertEquals(institution1.getId(), resultInstitution1.getId());
        assertEquals(institution1.getName(), resultInstitution1.getName());
        assertEquals(institution1.getCity(), resultInstitution1.getCity());
        assertEquals(institution1.getPostalCode(), resultInstitution1.getPostalCode());
        assertEquals(institution1.getStreet(), resultInstitution1.getStreet());
        assertEquals(institution1.getBuildingNumber(), resultInstitution1.getBuildingNumber());

        Institution resultInstitution2 = result.get(1);
        assertEquals(institution2.getId(), resultInstitution2.getId());
        assertEquals(institution2.getName(), resultInstitution2.getName());
        assertEquals(institution2.getCity(), resultInstitution2.getCity());
        assertEquals(institution2.getPostalCode(), resultInstitution2.getPostalCode());
        assertEquals(institution2.getStreet(), resultInstitution2.getStreet());
        assertEquals(institution2.getBuildingNumber(), resultInstitution2.getBuildingNumber());
    }

    @Test
    void getAssignedInstitutionsForDoctor_NonExistentDoctor_IllegalArhumentExceptionThrown() {
        // given
        Long nonExsitenId = 12345L;

        // when + then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> doctorService.getAssignedInstitutionsForDoctor(nonExsitenId));
        assertEquals("Doctor not found.", exception.getMessage());
        verify(doctorRepository, times(1)).findById(nonExsitenId);
    }
}