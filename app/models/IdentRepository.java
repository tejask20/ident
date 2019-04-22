package models;

import java.util.*;

public class IdentRepository {
    private static IdentRepository identRepository;
    Set<Ident> idents = new HashSet<>();

    public static IdentRepository getIdentRepository()
    {
        if (identRepository == null) {
            identRepository = new IdentRepository();
        }
        return identRepository;
    }

    public void addIdent(Ident ident) {
        idents.add(ident);
    }

    public Ident getIdent(int id) {
        Ident ident = idents.stream()
                .filter(x -> id == x.getId())
                .findAny()
                .orElse(null);
        return ident;
    }

    public List<Ident> getAllIdents() {
        return new ArrayList<Ident>(idents);
    }
}
