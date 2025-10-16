import styles from "./Home.module.css";
import { useAuth } from "../../security/AuthContext";

export default function Home() {
  const { user } = useAuth();
  return (
    <section className={styles.page}>
      <div className={styles.card}>
        <h2>Dobrodošao{user ? `, ${user.firstName} ${user.lastName}` : ""}!</h2>
        <p>Ovo je početna stranica. Biće zaštićena dok si ulogovan.</p>
      </div>
    </section>
  );
}
