package rs.ac.fon.gymtracker.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.domain.ServicePackage;
import rs.ac.fon.gymtracker.repository.MemberRepository;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository repo;

    @Mock
    private ServicePackageRepository packageRepo;

    @InjectMocks
    private MemberServiceImpl service;

    private Member base;

    @BeforeEach
    void setUp() {
        base = new Member();
        base.setId(null);
        base.setFirstName("Jovan");
        base.setLastName("Jovanović");
        base.setEmail("jovan@example.com");
        base.setServicePackage(null);
    }

    @AfterEach
    void tearDown() {
        base = null;
    }

    private static Member m(Long id, String fn, String ln, String email, ServicePackage pkg) {
        var x = new Member();
        x.setId(id);
        x.setFirstName(fn);
        x.setLastName(ln);
        x.setEmail(email);
        x.setServicePackage(pkg);
        return x;
    }

    private static ServicePackage pkg(Long id, String name) {
        var p = new ServicePackage();
        p.setId(id);
        p.setName(name);
        return p;
    }

    @Test
    void testCreate() {
        var toSave = base;
        var saved = m(1L, "Jovan", "Jovanović", "jovan@example.com", null);

        when(repo.save(toSave)).thenReturn(saved);

        var out = service.create(toSave);

        assertEquals(1L, out.getId());
        assertEquals("Jovan", out.getFirstName());

        verify(repo).save(toSave);
    }

    @Test
    void testFindByIdFound() {
        when(repo.findById(2L)).thenReturn(Optional.of(m(2L, "Ana", "Anić", "ana@example.com", null)));

        var out = service.findById(2L);

        assertTrue(out.isPresent());
        assertEquals("Ana", out.get().getFirstName());

        verify(repo).findById(2L);
    }

    @Test
    void testFindByIdMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        var out = service.findById(99L);

        assertTrue(out.isEmpty());

        verify(repo).findById(99L);
    }

    @Test
    void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(
                m(1L, "Jovan", "Jovanović", "jovan@example.com", null),
                m(2L, "Ana", "Anić", "ana@example.com", null)
        ));

        var out = service.findAll();

        assertEquals(2, out.size());
        assertEquals("Ana", out.get(1).getFirstName());

        verify(repo).findAll();
    }

    @Test
    void testUpdateMergesNonNullFieldsOnly() {
        var current = m(3L, "Petar", "Petrović", "petar@example.com", null);

        when(repo.findById(3L)).thenReturn(Optional.of(current));
        when(repo.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        var patch = new Member();
        patch.setEmail("new@example.com");

        var updated = service.update(3L, patch);

        assertEquals(3L, updated.getId());
        assertEquals("Petar", updated.getFirstName());
        assertEquals("new@example.com", updated.getEmail());

        verify(repo).findById(3L);
        verify(repo).save(current);
    }

    @Test
    void testUpdateWhenMissingThrows() {
        when(repo.findById(77L)).thenReturn(Optional.empty());

        var patch = new Member();
        patch.setFirstName("X");

        assertThrows(NoSuchElementException.class, () -> service.update(77L, patch));

        verify(repo).findById(77L);
    }

    @Test
    void testDeleteById() {
        service.deleteById(55L);
        verify(repo).deleteById(55L);
    }

    @Test
    void testChangePackageSuccess() {
        var member = m(10L, "Jovan", "Jovanović", "jovan@example.com", null);
        var pkg = pkg(5L, "Premium");

        when(repo.findById(10L)).thenReturn(Optional.of(member));
        when(packageRepo.findById(5L)).thenReturn(Optional.of(pkg));
        when(repo.save(any(Member.class))).thenAnswer(inv -> inv.getArgument(0));

        var out = service.changePackage(10L, 5L);

        assertEquals("Premium", out.getServicePackage().getName());

        verify(repo).findById(10L);
        verify(packageRepo).findById(5L);
        verify(repo).save(member);
    }

    @Test
    void testChangePackageMissingMemberThrows() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.changePackage(99L, 1L));

        verify(repo).findById(99L);
    }

    @Test
    void testChangePackageMissingPackageThrows() {
        var member = m(10L, "Jovan", "Jovanović", "jovan@example.com", null);
        when(repo.findById(10L)).thenReturn(Optional.of(member));
        when(packageRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.changePackage(10L, 99L));

        verify(repo).findById(10L);
        verify(packageRepo).findById(99L);
    }

    @Test
    void testSearchByTextOnly() {
        var result = List.of(m(1L, "Jovan", "Jovanović", "jovan@example.com", null));
        doReturn(result).when(repo).findAll((Specification<Member>) any());

        var out = service.search("jovan", null);

        assertEquals(1, out.size());
        assertEquals("Jovan", out.get(0).getFirstName());

        verify(repo).findAll(any(Specification.class));
    }

    @Test
    void testSearchByPackageOnly() {
        var result = List.of(m(2L, "Ana", "Anić", "ana@example.com", pkg(1L, "Basic")));
        doReturn(result).when(repo).findAll((Specification<Member>) any());

        var out = service.search(null, 1L);

        assertEquals(1, out.size());
        assertEquals("Ana", out.get(0).getFirstName());

        verify(repo).findAll(any(Specification.class));
    }

    @Test
    void testSearchByTextAndPackage() {
        var result = List.of(m(3L, "Petar", "Petrović", "petar@example.com", pkg(2L, "Advanced")));
        doReturn(result).when(repo).findAll((Specification<Member>) any());

        var out = service.search("petar", 2L);

        assertEquals(1, out.size());
        assertEquals("Petar", out.get(0).getFirstName());

        verify(repo).findAll(any(Specification.class));
    }

    @Test
    void testSearchWithNoCriteriaReturnsAll() {
        var result = List.of(m(4L, "Luka", "Lukić", "luka@example.com", null));
        doReturn(result).when(repo).findAll((Specification<Member>) any());

        var out = service.search(null, null);

        assertEquals(1, out.size());
        assertEquals("Luka", out.get(0).getFirstName());

        verify(repo).findAll((Specification<Member>) any());
    }

}