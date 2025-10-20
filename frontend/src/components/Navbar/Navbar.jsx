import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../../security/AuthContext";
import styles from "./Navbar.module.css";

const link = ({ isActive }) =>
  isActive ? `${styles.link} ${styles.active}` : styles.link;

export default function Navbar() {
  const { user, logout } = useAuth();
  const nav = useNavigate();

  function onLogout() {
    logout();
    nav("/login", { replace: true });
  }

  return (
    <nav className={styles.nav}>
      <NavLink to="/" className={link}>
        Početna
      </NavLink>
      <NavLink to="/trainers" className={link}>
        Treneri
      </NavLink>
      <NavLink to="/members" className={link}>
        Članovi
      </NavLink>
      <NavLink to="/training-records" className={link}>
        Evidencije
      </NavLink>
      <NavLink to="/certificates" className={link}>
        Sertifikati
      </NavLink>

      <div className={styles.sep} />
      {user && (
        <span className={styles.who}>
          Ulogovan: {user.firstName} {user.lastName}
        </span>
      )}
      <button className={styles.logout} onClick={onLogout}>
        Odjava
      </button>
    </nav>
  );
}
