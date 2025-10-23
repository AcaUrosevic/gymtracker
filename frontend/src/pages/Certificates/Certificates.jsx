import { useEffect, useState } from "react";
import Container from "../../components/Container/Container";
import Card from "../../components/Card/Card";
import Button from "../../components/Button/Button";
import { useToast } from "../../components/Toast/ToastContext";
import styles from "./Certificates.module.css";
import { listCertificates, createCertificate } from "../../lib/certificatesApi";

function CertificateForm({ onSaved, onCancel, loading }) {
  const [name, setName] = useState("");
  const [type, setType] = useState("");

  const [eName, setEName] = useState("");
  const [eType, setEType] = useState("");

  function validate() {
    let ok = true;
    setEName("");
    setEType("");

    if (!name.trim()) {
      setEName("Naziv je obavezan.");
      ok = false;
    } else if (name.trim().length > 120) {
      setEName("Max 120 karaktera.");
      ok = false;
    }

    if (!type.trim()) {
      setEType("Tip je obavezan.");
      ok = false;
    } else if (type.trim().length > 80) {
      setEType("Max 80 karaktera.");
      ok = false;
    }

    return ok;
  }

  async function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    await onSaved({ name: name.trim(), type: type.trim() });
    setName("");
    setType("");
  }

  return (
    <form className={styles.form} onSubmit={onSubmit} noValidate>
      <label className={styles.label}>
        Naziv sertifikata
        <input
          className={styles.input}
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        {eName && <span className={styles.fieldError}>{eName}</span>}
      </label>
      <label className={styles.label}>
        Tip / oblast
        <input
          className={styles.input}
          value={type}
          onChange={(e) => setType(e.target.value)}
        />
        {eType && <span className={styles.fieldError}>{eType}</span>}
      </label>

      <div className={styles.row}>
        <Button variant="ghost" type="button" onClick={onCancel}>
          Otkaži
        </Button>
        <Button type="submit" disabled={loading}>
          {loading ? "Snimanje…" : "Ubaci sertifikat"}
        </Button>
      </div>
    </form>
  );
}

export default function Certificates() {
  const { addToast } = useToast();
  const [certs, setCerts] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [loadingCreate, setLoadingCreate] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const data = await listCertificates();
        setCerts(data);
      } catch {
        addToast("Ne mogu da učitam sertifikat.", {
          duration: 3000,
        });
      }
    })();
  }, [addToast]);

  async function handleSave(payload) {
    setLoadingCreate(true);
    try {
      const saved = await createCertificate(payload);
      setCerts((cs) => [saved, ...cs]);
      setShowForm(false);
      addToast("Sistem je zapamtio sertifikat.");
    } catch {
      addToast("Sistem ne može da zapamti sertifikat.", { duration: 3000 });
    } finally {
      setLoadingCreate(false);
    }
  }

  return (
    <section className={styles.page}>
      <Container>
        <div className={styles.grid}>
          <Card title="Sertifikati">
            <div className={styles.list}>
              {certs.map((c) => (
                <div key={c.id} className={styles.rowItem}>
                  <div>
                    <div className={styles.name}>{c.name}</div>
                    <div className={styles.meta}>Tip: {c.type}</div>
                  </div>
                </div>
              ))}
              {certs.length === 0 && (
                <div className={styles.empty}>Još nema sertifikata.</div>
              )}
            </div>
          </Card>

          <Card title="Ubaci sertifikat">
            {!showForm ? (
              <Button
                as="button"
                type="button"
                onClick={() => setShowForm(true)}
              >
                Novi sertifikat
              </Button>
            ) : (
              <CertificateForm
                onSaved={handleSave}
                onCancel={() => setShowForm(false)}
                loading={loadingCreate}
              />
            )}
          </Card>
        </div>
      </Container>
    </section>
  );
}
