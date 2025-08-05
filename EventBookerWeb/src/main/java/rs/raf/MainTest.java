package rs.raf;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class MainTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("eventBookerPU");
        System.out.println("Uspostavljena konekcija sa bazom!");
        emf.close();
    }
}
