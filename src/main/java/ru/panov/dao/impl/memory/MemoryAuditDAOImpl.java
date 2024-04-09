package ru.panov.dao.impl.memory;

import ru.panov.dao.AuditDAO;
import ru.panov.model.Audit;

import java.util.*;

import static ru.panov.util.AutoIncrementUtil.increment;

public class MemoryAuditDAOImpl implements AuditDAO {
    private final Map<Long, Audit> audits = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Optional<Audit> findById(Long id) {
        Audit audit = audits.get(id);
        return Optional.ofNullable(audit);
    }

    @Override
    public List<Audit> findAll() {
        return List.copyOf(audits.values());
    }

    @Override
    public Audit save(Audit audit) {
        audit.setId(increment(audits));
        audits.put(audit.getId(), audit);
        return audit;
    }
}