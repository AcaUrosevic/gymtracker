import { useEffect, useMemo, useState } from "react";
import styles from "./Members.module.css";
import Container from "../../components/Container/Container";
import Card from "../../components/Card/Card";
import Button from "../../components/Button/Button";
import { useToast } from "../../components/Toast/ToastContext";
import {
  listPackages,
  searchMembers,
  createMember,
  updateMember,
  deleteMember,
} from "../../lib/membersApi";

function MemberForm({ initial, packages, onCancel, onSaved, loading }) {
  const [firstName, setFirstName] = useState(initial?.firstName || "");
  const [lastName, setLastName] = useState(initial?.lastName || "");
  const [email, setEmail] = useState(initial?.email || "");
  const [packageId, setPackageId] = useState(initial?.packageId || "");
  const [eFirst, setEFirst] = useState("");
  const [eLast, setELast] = useState("");
  const [eEmail, setEEmail] = useState("");

  useEffect(() => {
    setFirstName(initial?.firstName || "");
    setLastName(initial?.lastName || "");
    setEmail(initial?.email || "");
    setPackageId(initial?.packageId || "");
    setEFirst("");
    setELast("");
    setEEmail("");
  }, [initial]);

  function validate() {
    let ok = true;
    setEFirst("");
    setELast("");
    setEEmail("");
    if (!firstName.trim()) {
      setEFirst("Ime je obavezno.");
      ok = false;
    }
    if (!lastName.trim()) {
      setELast("Prezime je obavezno.");
      ok = false;
    }
    if (email && !/^\S+@\S+\.\S+$/.test(email)) {
      setEEmail("Email nije ispravan.");
      ok = false;
    }
    return ok;
  }

  function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    onSaved({
      firstName: firstName.trim(),
      lastName: lastName.trim(),
      email: email.trim() || null,
      packageId: packageId || null,
    });
  }

  return (
    <form className={styles.form} onSubmit={onSubmit} noValidate>
      <label className={styles.label}>
        Ime
        <input
          className={styles.input}
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        {eFirst && <span className={styles.fieldError}>{eFirst}</span>}
      </label>
      <label className={styles.label}>
        Prezime
        <input
          className={styles.input}
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        {eLast && <span className={styles.fieldError}>{eLast}</span>}
      </label>
      <label className={styles.label}>
        Email (opciono)
        <input
          className={styles.input}
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        {eEmail && <span className={styles.fieldError}>{eEmail}</span>}
      </label>
      <label className={styles.label}>
        Paket usluga (opciono)
        <select
          className={styles.input}
          value={packageId || ""}
          onChange={(e) =>
            setPackageId(e.target.value ? Number(e.target.value) : "")
          }
        >
          <option value="">— bez paketa —</option>
          {packages.map((p) => (
            <option key={p.id} value={p.id}>
              {p.name}
            </option>
          ))}
        </select>
      </label>

      <div className={styles.row}>
        <Button variant="ghost" type="button" onClick={onCancel}>
          Otkaži
        </Button>
        <Button type="submit" disabled={loading}>
          {loading ? "Snimanje…" : "Sačuvaj"}
        </Button>
      </div>
    </form>
  );
}

function MembersList({ items, onPick, onRemove }) {
  return (
    <div className={styles.list}>
      {items.map((m) => (
        <div key={m.id} className={styles.rowItem}>
          <div className={styles.person}>
            <div className={styles.avatar}>
              {m.firstName?.[0] || "?"}
              {m.lastName?.[0] || ""}
            </div>
            <div>
              <div className={styles.name}>
                {m.firstName} {m.lastName}
              </div>
              <div className={styles.meta}>
                {m.email || "—"} {m.packageName ? `• ${m.packageName}` : ""}
              </div>
            </div>
          </div>
          <div className={styles.actionsInline}>
            <Button variant="ghost" onClick={() => onPick(m)}>
              Izmeni
            </Button>
            <Button variant="ghost" onClick={() => onRemove(m)}>
              Obriši
            </Button>
          </div>
        </div>
      ))}
      {items.length === 0 && (
        <div className={styles.empty}>Nema rezultata.</div>
      )}
    </div>
  );
}

export default function Members() {
  const { addToast } = useToast();

  const [query, setQuery] = useState("");
  const [pkgFilter, setPkgFilter] = useState("");
  const [packages, setPackages] = useState([]);
  const [members, setMembers] = useState([]);
  const [selected, setSelected] = useState(null);
  const [loading, setLoading] = useState(false);

  // ✅ OVO JE PRAVO MESTO ZA FLAG
  const [hasInteracted, setHasInteracted] = useState(false);

  useEffect(() => {
    (async () => {
      try {
        const [pkgs, list] = await Promise.all([
          listPackages(),
          searchMembers("", null),
        ]);
        setPackages(pkgs || []);
        setMembers(list || []);
      } catch {
        addToast("Ne mogu da učitam početne podatke.", { duration: 3000 });
      }
    })();
  }, [addToast]);

  const filtered = useMemo(() => members, [members]);

  async function doSearch(e, { notify = true } = {}) {
    e?.preventDefault();
    try {
      const data = await searchMembers(
        query,
        pkgFilter ? Number(pkgFilter) : null
      );
      setMembers(data);

      if (notify) {
        if (data.length > 0) {
          addToast(
            "Sistem je našao članove teretane po zadatim kriterijumima."
          );
        } else {
          addToast(
            "Sistem ne može da nađe članove teretane po zadatim kriterijumima.",
            { duration: 3000 }
          );
        }
      }
    } catch {
      if (notify) {
        addToast(
          "Sistem ne može da nađe članove teretane po zadatim kriterijumima.",
          { duration: 3000 }
        );
      }
    }
  }

  useEffect(() => {
    if (!hasInteracted) return;
    (async () => {
      await doSearch(undefined, { notify: true });
    })();
  }, [pkgFilter]);

  function onSubmitSearch(e) {
    e.preventDefault();
    setHasInteracted(true);
    doSearch(undefined, { notify: true });
  }

  async function handleCreate() {
    setSelected({});
    addToast("Sistem je kreirao člana teretane.");
  }

  async function handleSave(payload) {
    setLoading(true);
    try {
      if (selected?.id) {
        const saved = await updateMember(selected.id, payload);
        setMembers((ms) => ms.map((m) => (m.id === saved.id ? saved : m)));
        addToast("Sistem je zapamtio člana teretane.");
      } else {
        const created = await createMember(payload);
        setMembers((ms) => [created, ...ms]);
        addToast("Sistem je zapamtio člana teretane.");
      }
      setSelected(null);
    } catch {
      addToast("Sistem ne može da zapamti člana teretane.", { duration: 3000 });
    } finally {
      setLoading(false);
    }
  }

  async function handlePick(m) {
    try {
      setSelected(m);
      addToast("Sistem je našao člana teretane.");
    } catch {
      addToast("Sistem ne može da nađe člana teretane.", { duration: 3000 });
    }
  }

  async function handleRemove(m) {
    try {
      await deleteMember(m.id);
      setMembers((ms) => ms.filter((x) => x.id !== m.id));
      addToast("Sistem je obrisao člana teretane.");
      if (selected?.id === m.id) setSelected(null);
    } catch {
      addToast("Sistem ne može da obriše člana teretane.", { duration: 3000 });
    }
  }

  return (
    <section className={styles.page}>
      <Container>
        <div className={styles.topbar}>
          <form onSubmit={onSubmitSearch} className={styles.search}>
            <input
              className={styles.input}
              placeholder="Pretraga po imenu, prezimenu ili emailu…"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />

            <select
              className={styles.input}
              value={pkgFilter}
              onChange={(e) => {
                setPkgFilter(e.target.value);
                setHasInteracted(true);
              }}
              title="Paket usluga"
            >
              <option value="">Svi paketi</option>
              {packages.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name}
                </option>
              ))}
            </select>

            <Button as="button" type="submit">
              Pretraži
            </Button>
            <Button
              as="button"
              type="button"
              variant="ghost"
              onClick={handleCreate}
            >
              Kreiraj člana
            </Button>
          </form>
        </div>

        <div className={styles.columns}>
          <Card title="Rezultati pretrage">
            <MembersList
              items={filtered}
              onPick={handlePick}
              onRemove={handleRemove}
            />
          </Card>

          <Card title={selected?.id ? "Izmeni člana" : "Novi član"}>
            {!selected ? (
              <div className={styles.empty}>
                Izaberi člana iz liste ili kreiraj novog.
              </div>
            ) : (
              <MemberForm
                initial={selected}
                packages={packages}
                onCancel={() => setSelected(null)}
                onSaved={handleSave}
                loading={loading}
              />
            )}
          </Card>
        </div>
      </Container>
    </section>
  );
}
