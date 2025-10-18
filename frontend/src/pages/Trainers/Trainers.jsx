import { useEffect, useMemo, useState } from "react";
import Container from "../../components/Container/Container";
import Card from "../../components/Card/Card";
import Button from "../../components/Button/Button";
import { useToast } from "../../components/Toast/ToastContext";
import styles from "./Trainers.module.css";

import { listTrainers } from "../../lib/trainersApi";
import { listCertificates } from "../../lib/certificatesApi";
import {
  listTrainerCertificates,
  assignTrainerCertificate,
  revokeTrainerCertificate,
  listTrainersWithCertificates,
} from "../../lib/trainerCertificatesApi";

export default function Trainers() {
  const { addToast } = useToast();

  const [trainers, setTrainers] = useState([]);
  const [certificates, setCertificates] = useState([]);
  const [selected, setSelected] = useState(null);
  const [addingCert, setAddingCert] = useState(false);

  const [addCertId, setAddCertId] = useState("");
  const [addIssuedAt, setAddIssuedAt] = useState("");

  useEffect(() => {
    (async () => {
      try {
        const [ts, cs] = await Promise.all([
          listTrainersWithCertificates().catch(async () => {
            const pure = await listTrainers();
            const rows = await Promise.all(
              pure.map(async (t) => ({
                ...t,
                certificates: await listTrainerCertificates(t.id),
              }))
            );
            return rows;
          }),
          listCertificates(),
        ]);
        setTrainers(ts || []);
        setCertificates(cs || []);
      } catch {
        addToast("Ne mogu da učitam trenere ili sertifikate.", {
          duration: 3000,
        });
      }
    })();
  }, [addToast]);

  const trainersById = useMemo(
    () => Object.fromEntries((trainers || []).map((t) => [t.id, t])),
    [trainers]
  );

  async function openTrainer(t) {
    try {
      const certs = await listTrainerCertificates(t.id);
      setSelected({ trainer: t, certificates: certs });
    } catch {
      addToast("Ne mogu da učitam detalje trenera.", { duration: 3000 });
    }
  }

  function closeDetail() {
    setSelected(null);
    setAddCertId("");
    setAddIssuedAt("");
  }

  async function addCertToSelected() {
    if (!selected?.trainer?.id || !addCertId) return;

    const already = (selected.certificates || []).some(
      (c) => String(c.certificateId) === String(addCertId)
    );
    if (already) {
      addToast("Trener već poseduje taj sertifikat.", { duration: 3000 });
      return;
    }

    if (addIssuedAt) {
      const today = new Date().toISOString().slice(0, 10);
      if (addIssuedAt > today) {
        addToast("Datum izdavanja ne može biti u budućnosti.", {
          duration: 3000,
        });
        return;
      }
    }

    setAddingCert(true);
    try {
      await assignTrainerCertificate(selected.trainer.id, {
        certificateId: Number(addCertId),
        issuedAt: addIssuedAt || null,
      });

      const fresh = await listTrainerCertificates(selected.trainer.id);
      setSelected((s) => s && { ...s, certificates: fresh });
      setTrainers((ts) =>
        ts.map((t) =>
          t.id === selected.trainer.id ? { ...t, certificates: fresh } : t
        )
      );

      setAddCertId("");
      setAddIssuedAt("");
      addToast("Sistem je dodelio sertifikat treneru.");
    } catch (e) {
      if (e?.response?.status === 409) {
        addToast(
          e.response.data?.message || "Trener već poseduje taj sertifikat.",
          { duration: 3000 }
        );
      } else if (e?.response?.status === 400) {
        addToast("Neispravan datum izdavanja.", { duration: 3000 });
      } else {
        addToast("Sistem ne može da dodeli sertifikat.", { duration: 3000 });
      }
    } finally {
      setAddingCert(false);
    }
  }

  async function revokeCert(trainerId, certificateId) {
    if (!window.confirm("Ukloniti ovaj sertifikat od trenera?")) return;
    try {
      await revokeTrainerCertificate(trainerId, certificateId);
      const fresh = await listTrainerCertificates(trainerId);

      setSelected((s) =>
        s && s.trainer.id === trainerId ? { ...s, certificates: fresh } : s
      );
      setTrainers((ts) =>
        ts.map((t) => (t.id === trainerId ? { ...t, certificates: fresh } : t))
      );

      addToast("Sertifikat je uklonjen.");
    } catch {
      addToast("Sistem ne može da ukloni sertifikat.", { duration: 3000 });
    }
  }

  return (
    <section className={styles.page}>
      <Container>
        <div className={styles.columns}>
          <Card title="Treneri">
            <div className={styles.list}>
              {(trainers || []).map((t) => (
                <div key={t.id} className={styles.rowItem}>
                  <div>
                    <div className={styles.name}>
                      {t.firstName} {t.lastName}
                    </div>
                    <div className={styles.meta}>
                      {t.email || "—"} • korisnik: {t.username}
                    </div>

                    <div className={styles.badgeList} style={{ marginTop: 8 }}>
                      {(t.certificates || []).map((c) => (
                        <span
                          key={`${t.id}-${c.certificateId}`}
                          className={styles.badge}
                        >
                          <span>{c.certificateName}</span>
                          {c.issuedAt && <small>• {c.issuedAt}</small>}
                        </span>
                      ))}
                      {(!t.certificates || t.certificates.length === 0) && (
                        <span className={styles.meta}>Nema sertifikata.</span>
                      )}
                    </div>
                  </div>

                  <div className={styles.actionsInline}>
                    <Button variant="ghost" onClick={() => openTrainer(t)}>
                      Detalji
                    </Button>
                  </div>
                </div>
              ))}

              {(trainers || []).length === 0 && (
                <div className={styles.empty}>Nema trenera.</div>
              )}
            </div>
          </Card>

          <Card title="Detalj trenera">
            {!selected ? (
              <div className={styles.empty}>Izaberi trenera sa liste levo.</div>
            ) : (
              <>
                <div className={styles.box}>
                  <div className={styles.name}>
                    {selected.trainer.firstName} {selected.trainer.lastName}
                  </div>
                  <div className={styles.meta}>
                    {selected.trainer.email || "—"} • korisnik:{" "}
                    {selected.trainer.username}
                  </div>
                </div>

                <h3 className={styles.subheading}>Sertifikati</h3>
                <div className={styles.list}>
                  {(selected.certificates || []).map((c) => (
                    <div
                      key={`${selected.trainer.id}-${c.certificateId}`}
                      className={styles.rowItem}
                    >
                      <div>
                        <div className={styles.name}>{c.certificateName}</div>
                        <div className={styles.meta}>
                          {c.issuedAt ? `Izdato: ${c.issuedAt}` : "Bez datuma"}
                        </div>
                      </div>
                      <div className={styles.actionsInline}>
                        <Button
                          variant="ghost"
                          onClick={() =>
                            revokeCert(selected.trainer.id, c.certificateId)
                          }
                        >
                          Ukloni
                        </Button>
                      </div>
                    </div>
                  ))}
                  {(!selected.certificates ||
                    selected.certificates.length === 0) && (
                    <div className={styles.empty}>Nema sertifikata.</div>
                  )}
                </div>

                <div className={styles.box}>
                  <div className={styles.subheading}>
                    Dodaj sertifikat treneru
                  </div>
                  <div className={styles.formInline}>
                    <select
                      className={styles.input}
                      value={addCertId}
                      onChange={(e) => setAddCertId(e.target.value)}
                    >
                      <option value="">— izaberi sertifikat —</option>
                      {certificates.map((c) => (
                        <option key={c.id} value={c.id}>
                          {c.name}
                        </option>
                      ))}
                    </select>
                    <input
                      className={styles.input}
                      type="date"
                      value={addIssuedAt}
                      onChange={(e) => setAddIssuedAt(e.target.value)}
                      title="Datum izdavanja (opciono, ne u budućnosti)"
                    />
                    <Button
                      type="button"
                      onClick={addCertToSelected}
                      disabled={addingCert}
                    >
                      {addingCert ? "Dodavanje…" : "Dodaj"}
                    </Button>
                  </div>
                </div>

                <div className={styles.row} style={{ marginTop: 12 }}>
                  <Button variant="ghost" onClick={closeDetail}>
                    Zatvori
                  </Button>
                </div>
              </>
            )}
          </Card>
        </div>
      </Container>
    </section>
  );
}
