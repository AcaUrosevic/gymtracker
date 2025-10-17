package rs.ac.fon.gymtracker.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.repository.MemberRepository;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;
import rs.ac.fon.gymtracker.service.impl.MemberServiceImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock MemberRepository repo;
    @Mock ServicePackageRepository packageRepo;

    @InjectMocks MemberServiceImpl service;

    @Test
    void update_merges_non_null_fields_only() {
        var current = new Member();
        current.setFirstName("Petar");
        current.setLastName("Petrović");
        current.setEmail("old@example.com");

        when(repo.findById(5L)).thenReturn(Optional.of(current));
        when(repo.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Member();
        patch.setEmail("new@example.com");

        var updated = service.update(5L, patch);

        assertEquals("Petar", updated.getFirstName());
        assertEquals("Petrović", updated.getLastName());
        assertEquals("new@example.com", updated.getEmail());

        verify(repo).save(any(Member.class));
    }

    @Test
    void changePackage_sets_new_package() {
        var m = new Member(); m.setId(7L);
        var p = new ServicePackage(); p.setId(2L);

        when(repo.findById(7L)).thenReturn(Optional.of(m));
        when(packageRepo.findById(2L)).thenReturn(Optional.of(p));
        when(repo.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.changePackage(7L, 2L);

        assertSame(p, result.getServicePackage());
        verify(repo).save(m);
    }
}
