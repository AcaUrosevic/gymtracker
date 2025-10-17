import Header from "../Header/Header";
import styles from "./Layout.module.css";

export default function Layout({ children }) {
  return (
    <div className={styles.wrap}>
      <Header />
      <main className={styles.main}>{children}</main>
      <footer className={styles.footer}>
        © {new Date().getFullYear()} GymTracker
      </footer>
    </div>
  );
}
