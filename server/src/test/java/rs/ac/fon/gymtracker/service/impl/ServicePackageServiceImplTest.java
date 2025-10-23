package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicePackageServiceImplTest {

    @Mock
    private ServicePackageRepository repo;

    @InjectMocks
    private ServicePackageServiceImpl service;

    private ServicePackage base;

    @BeforeEach
    void setUp() {
        base = new ServicePackage();
        base.setId(null);
        base.setName("Monthly");
        base.setDescription("30 days access");
        base.setDurationDays(30);
        base.setPrice(3000);
    }

    @AfterEach
    void tearDown() {
        base = null;
    }

    private static ServicePackage sp(Long id, String name, String desc, int days, int price) {
        var p = new ServicePackage();
        p.setId(id);
        p.setName(name);
        p.setDescription(desc);
        p.setDurationDays(days);
        p.setPrice(price);
        return p;
    }

    @Test
    void testCreate() {
        var toSave = base;
        var saved  = sp(10L, "Monthly", "30 days access", 30, 3000);

        when(repo.save(toSave)).thenReturn(saved);

        var out = service.create(toSave);

        assertEquals(10L, out.getId());
        assertEquals("Monthly", out.getName());
        assertEquals(30, out.getDurationDays());
        assertEquals(3000, out.getPrice());

        verify(repo).save(toSave);
    }

    @Test
    void testFindByIdFound() {
        when(repo.findById(5L)).thenReturn(Optional.of(sp(5L, "Quarter", "90 days", 90, 8000)));

        var out = service.findById(5L);

        assertTrue(out.isPresent());
        assertEquals(5L, out.get().getId());
        assertEquals("Quarter", out.get().getName());

        verify(repo).findById(5L);
    }

    @Test
    void testFindByIdMissing() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var out = service.findById(77L);

        assertTrue(out.isEmpty());

        verify(repo).findById(77L);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(
                sp(1L, "Monthly", "30", 30, 3000),
                sp(2L, "Annual", "365", 365, 25000)
        ));

        var out = service.findAll();

        assertEquals(2, out.size());
        assertEquals("Annual", out.get(1).getName());

        verify(repo).findAll();
    }

    @Test
    void testUpdateMergesNonNullAndPositiveOnly() {
        var current = sp(3L, "Monthly", "30", 30, 3000);

        when(repo.findById(3L)).thenReturn(Optional.of(current));
        when(repo.save(any(ServicePackage.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new ServicePackage();
        patch.setName("Monthly Plus");
        patch.setDescription(null);
        patch.setDurationDays(45);
        patch.setPrice(5000);

        var updated = service.update(3L, patch);

        assertEquals(3L, updated.getId());
        assertEquals("Monthly Plus", updated.getName());
        assertEquals("30", updated.getDescription()); // ostaje staro
        assertEquals(45, updated.getDurationDays());
        assertEquals(5000, updated.getPrice());

        verify(repo).findById(3L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateDoesNotOverwriteWithZeroOrNegative() {
        var current = sp(4L, "Quarter", "90", 90, 8000);

        when(repo.findById(4L)).thenReturn(Optional.of(current));
        when(repo.save(any(ServicePackage.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new ServicePackage();
        patch.setName(null);           // null => ne diraj
        patch.setDescription("upd");   // non-null => promeni
        patch.setDurationDays(0);      // 0 => IGNORIŠE se zbog >0 pravila
        patch.setPrice(0);             // 0 => IGNORIŠE se zbog >0 pravila

        var updated = service.update(4L, patch);

        assertEquals(4L, updated.getId());
        assertEquals("Quarter", updated.getName());   // ostaje
        assertEquals("upd", updated.getDescription()); // promenjeno
        assertEquals(90, updated.getDurationDays());   // nije promenjeno (0 ignorisano)
        assertEquals(8000, updated.getPrice());        // nije promenjeno (0 ignorisano)

        verify(repo).findById(4L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateWhenMissingThrows() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        var patch = new ServicePackage();
        patch.setName("X");

        assertThrows(NoSuchElementException.class, () -> service.update(99L, patch));

        verify(repo).findById(99L);
    }

    @Test
    void testDeleteById() {
        service.deleteById(44L);
        verify(repo).deleteById(44L);
    }
}
