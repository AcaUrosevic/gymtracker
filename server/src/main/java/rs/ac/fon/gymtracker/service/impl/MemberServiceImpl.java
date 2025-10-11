package rs.ac.fon.gymtracker.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.repository.MemberRepository;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;
import rs.ac.fon.gymtracker.service.MemberService;

import java.util.List;

@Service
@Transactional
public class MemberServiceImpl
        extends AbstractJpaCrudService<Member, Long, MemberRepository>
        implements MemberService {

    private final ServicePackageRepository packageRepo;

    public MemberServiceImpl(MemberRepository repo, ServicePackageRepository packageRepo) {
        super(repo);
        this.packageRepo = packageRepo;
    }

    @Override
    protected Member doMergeAndSave(Member current, Member patch) {
        if (patch.getFirstName() != null) current.setFirstName(patch.getFirstName());
        if (patch.getLastName() != null)  current.setLastName(patch.getLastName());
        if (patch.getEmail() != null)     current.setEmail(patch.getEmail());
        if (patch.getServicePackage() != null) current.setServicePackage(patch.getServicePackage());
        return repo.save(current);
    }

    @Override
    public Member changePackage(Long memberId, Long packageId) {
        var m = repo.findById(memberId).orElseThrow();
        var p = packageRepo.findById(packageId).orElseThrow();
        m.setServicePackage(p);
        return repo.save(m);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> searchByName(String query) {
        return repo.findByLastNameContainingIgnoreCaseOrFirstNameContainingIgnoreCase(query, query);
    }
}
