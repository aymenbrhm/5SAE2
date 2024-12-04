package tn.esprit.eventsproject.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.eventsproject.entities.*;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EventServicesImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private LogisticsRepository logisticsRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    private Event event;
    private Participant participant;
    private Logistics logistics;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks

        // Initialize test objects
        event = new Event();
        event.setDescription("Test Event");

        participant = new Participant();
        participant.setNom("Tounsi");
        participant.setPrenom("Ahmed");
        participant.setTache(Tache.ORGANISATEUR);

        logistics = new Logistics();
        logistics.setReserve(true);
        logistics.setPrixUnit(50.0f);
        logistics.setQuantite(2);
    }

    @Test
    void testAddParticipant() {
        when(participantRepository.save(participant)).thenReturn(participant);

        Participant savedParticipant = eventServices.addParticipant(participant);

        assertNotNull(savedParticipant);
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    void testAddAffectEvenParticipantWithEvent() {
        when(participantRepository.findById(participant.getIdPart())).thenReturn(java.util.Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        eventServices.addAffectEvenParticipant(event, participant.getIdPart());

        assertTrue(participant.getEvents().contains(event));
        verify(participantRepository, times(1)).findById(participant.getIdPart());
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAddAffectEvenParticipantWithParticipants() {
        Set<Participant> participants = new HashSet<>();
        participants.add(participant);
        event.setParticipants(participants);

        when(participantRepository.findById(participant.getIdPart())).thenReturn(java.util.Optional.of(participant));
        when(eventRepository.save(event)).thenReturn(event);

        eventServices.addAffectEvenParticipant(event);

        assertTrue(participant.getEvents().contains(event));
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    void testAddAffectLogWithEventDescription() {
        when(eventRepository.findByDescription("Test Event")).thenReturn(event);
        when(logisticsRepository.save(logistics)).thenReturn(logistics);

        eventServices.addAffectLog(logistics, "Test Event");

        assertTrue(event.getLogistics().contains(logistics));
        verify(eventRepository, times(1)).findByDescription("Test Event");
        verify(logisticsRepository, times(1)).save(logistics);
    }

    @Test
    void testGetLogisticsDates() {
        LocalDate startDate = LocalDate.of(2024, 12, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(List.of(event));

        List<Logistics> logisticsList = eventServices.getLogisticsDates(startDate, endDate);

        assertNotNull(logisticsList);
        assertTrue(logisticsList.isEmpty()); // Initially logistics list should be empty
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    void testCalculCout() {
        List<Event> events = List.of(event);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(event)).thenReturn(event);

        eventServices.calculCout();

        assertEquals(100.0f, event.getCout()); // Calculated cost should be 50 * 2
        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache("Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(event);
    }
}
