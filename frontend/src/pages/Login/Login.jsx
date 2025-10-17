import { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import api from "../../lib/api";
import { useAuth } from "../../security/AuthContext";
import styles from "./Login.module.css";
import { useToast } from "../../components/Toast/ToastContext";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [uErr, setUErr] = useState("");
  const [pErr, setPErr] = useState("");

  const { setToken, setUser } = useAuth();
  const nav = useNavigate();
  const { state } = useLocation();
  const { addToast } = useToast();

  function validate() {
    const u = username.trim();
    let ok = true;
    setUErr("");
    setPErr("");
    setError("");

    if (!u) {
      setUErr("Korisničko ime je obavezno.");
      ok = false;
    }
    if (!password) {
      setPErr("Lozinka je obavezna.");
      ok = false;
    }
    return ok;
  }

  async function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      const { token, user } =
        (
          await api.post("/trainers/login", {
            username: username.trim(),
            password,
          })
        ).data || {};
      if (!token || !user?.username)
        throw new Error("Neočekivan odgovor sa servera");
      setToken(token);
      setUser(user);

      addToast("Korisnicko ime i sifra su ispravni");

      try {
        nav(state?.from?.pathname || "/", { replace: true });
      } catch {
        setError("Ne moze da se otvori glavna forma i meni");
      }
    } catch (err) {
      const msg =
        err?.response?.status === 400
          ? "Korisnicko ime i sifra nisu ispravni."
          : err?.response?.data?.message || "Neuspesna prijava";
      setError(msg);
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className={styles.wrap}>
      <form className={styles.card} onSubmit={onSubmit} noValidate>
        <h1 className={styles.title}>Prijava</h1>
        <p className={styles.subtitle}>Pristup sistemu teretane</p>

        {error && <div className={styles.error}>{error}</div>}

        <label className={styles.label}>
          Korisničko ime
          <input
            className={styles.input}
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="aca_uros"
            aria-invalid={!!uErr}
          />
          {uErr && <span className={styles.fieldError}>{uErr}</span>}
        </label>

        <label className={styles.label}>
          Lozinka
          <input
            className={styles.input}
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            aria-invalid={!!pErr}
          />
          {pErr && <span className={styles.fieldError}>{pErr}</span>}
        </label>

        <button className={styles.button} disabled={loading}>
          {loading ? "Prijavljivanje…" : "Prijavi se"}
        </button>
      </form>
    </div>
  );
}
