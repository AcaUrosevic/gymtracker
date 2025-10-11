package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;
import rs.ac.fon.gymtracker.service.impl.ServicePackageServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicePackageServiceImplTest {

    @Mock ServicePackageRepository repo;
    @InjectMocks ServicePackageServiceImpl service;

    @Test
    void update_merges_non_null_fields_only() {
        var current = new ServicePackage();
        current.setName("Monthly");
        current.setDescription("old");
        current.setDurationDays(30);
        current.setPrice(3000);

        when(repo.findById(1L)).thenReturn(Optional.of(current));
        when(repo.save(any(ServicePackage.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new ServicePackage();
        patch.setDescription("new");

        var updated = service.update(1L, patch);

        assertEquals("Monthly", updated.getName());
        assertEquals("new", updated.getDescription());
        assertEquals(30, updated.getDurationDays());
        assertEquals(3000, updated.getPrice());

        verify(repo).save(any(ServicePackage.class));
    }
}
