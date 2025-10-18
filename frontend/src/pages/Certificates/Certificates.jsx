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
      setEName("Назив је обавезан.");
      ok = false;
    } else if (name.trim().length > 120) {
      setEName("Макс. 120 карактера.");
      ok = false;
    }

    if (!type.trim()) {
      setEType("Тип је обавезан.");
      ok = false;
    } else if (type.trim().length > 80) {
      setEType("Макс. 80 карактера.");
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
        Назив сертификата
        <input
          className={styles.input}
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        {eName && <span className={styles.fieldError}>{eName}</span>}
      </label>
      <label className={styles.label}>
        Тип / област
        <input
          className={styles.input}
          value={type}
          onChange={(e) => setType(e.target.value)}
        />
        {eType && <span className={styles.fieldError}>{eType}</span>}
      </label>

      <div className={styles.row}>
        <Button variant="ghost" type="button" onClick={onCancel}>
          Откажи
        </Button>
        <Button type="submit" disabled={loading}>
          {loading ? "Снимање…" : "Убаци сертификат"}
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
        addToast("Не могу да учитам податке о сертификатима.", {
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
      addToast("Систем је запамтио сертификат.");
    } catch {
      addToast("Систем не може да запамти сертификат.", { duration: 3000 });
    } finally {
      setLoadingCreate(false);
    }
  }

  return (
    <section className={styles.page}>
      <Container>
        <div className={styles.grid}>
          <Card title="Сертификати">
            <div className={styles.list}>
              {certs.map((c) => (
                <div key={c.id} className={styles.rowItem}>
                  <div>
                    <div className={styles.name}>{c.name}</div>
                    <div className={styles.meta}>Тип: {c.type}</div>
                  </div>
                </div>
              ))}
              {certs.length === 0 && (
                <div className={styles.empty}>Још нема сертификата.</div>
              )}
            </div>
          </Card>

          <Card title="Убаци сертификат">
            {!showForm ? (
              <Button
                as="button"
                type="button"
                onClick={() => setShowForm(true)}
              >
                Нови сертификат
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
