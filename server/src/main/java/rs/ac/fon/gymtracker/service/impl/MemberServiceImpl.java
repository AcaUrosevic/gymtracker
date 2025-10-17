package rs.ac.fon.gymtracker.service.impl;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.ac.fon.gymtracker.domain.Member;
import rs.ac.fon.gymtracker.repository.MemberRepository;
import rs.ac.fon.gymtracker.repository.ServicePackageRepository;
import rs.ac.fon.gymtracker.service.MemberService;

import java.util.ArrayList;
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
    public List<Member> search(String q, Long packageId) {
        final List<Specification<Member>> specs = new ArrayList<>();

        if (q != null && !q.isBlank()) {
            final String like = "%" + q.toLowerCase() + "%";

            Specification<Member> textSpec = Specification.anyOf(
                    (root, cq, cb) -> cb.like(cb.lower(root.get("firstName")), like),
                    (root, cq, cb) -> cb.like(cb.lower(root.get("lastName")),  like),
                    (root, cq, cb) -> cb.like(cb.lower(cb.coalesce(root.get("email"), "")), like)
            );

            specs.add(textSpec);
        }

        if (packageId != null) {
            Specification<Member> pkgSpec = (root, cq, cb) ->
                    cb.equal(root.join("servicePackage", JoinType.LEFT).get("id"), packageId);
            specs.add(pkgSpec);
        }

        Specification<Member> finalSpec = Specification.allOf(specs);
        return repo.findAll(finalSpec);
    }
}
