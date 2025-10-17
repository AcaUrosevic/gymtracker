import { useEffect, useMemo, useState } from "react";
import Container from "../../components/Container/Container";
import Card from "../../components/Card/Card";
import Button from "../../components/Button/Button";
import { useToast } from "../../components/Toast/ToastContext";
import styles from "./TrainingRecords.module.css";

import {
  listTrainers,
  listMembers,
  listExercises,
  searchRecords,
  createRecord,
  updateRecord,
  getRecord,
  deleteRecord,
} from "../../lib/trainingRecordsApi";

function RecordHeaderForm({
  initial,
  trainers,
  members,
  onCancel,
  onSaved,
  loading,
}) {
  const [date, setDate] = useState(initial?.trainingDate || "");
  const [trainerId, setTrainerId] = useState(initial?.trainerId || "");
  const [memberId, setMemberId] = useState(initial?.memberId || "");

  const [eDate, setEDate] = useState("");
  const [eTrainer, setETrainer] = useState("");
  const [eMember, setEMember] = useState("");

  useEffect(() => {
    setDate(initial?.trainingDate || "");
    setTrainerId(initial?.trainerId || "");
    setMemberId(initial?.memberId || "");
    setEDate("");
    setETrainer("");
    setEMember("");
  }, [initial]);

  function validate() {
    let ok = true;
    setEDate("");
    setETrainer("");
    setEMember("");
    if (!date) {
      setEDate("Datum je obavezan.");
      ok = false;
    }
    if (!trainerId) {
      setETrainer("Trener je obavezan.");
      ok = false;
    }
    if (!memberId) {
      setEMember("Član je obavezan.");
      ok = false;
    }
    return ok;
  }

  function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    onSaved({ date, trainerId: Number(trainerId), memberId: Number(memberId) });
  }

  return (
    <form className={styles.form} onSubmit={onSubmit} noValidate>
      <label className={styles.label}>
        Datum treninga
        <input
          className={styles.input}
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />
        {eDate && <span className={styles.fieldError}>{eDate}</span>}
      </label>

      <label className={styles.label}>
        Trener
        <select
          className={styles.input}
          value={trainerId}
          onChange={(e) => setTrainerId(e.target.value)}
        >
          <option value="">— izaberi trenera —</option>
          {trainers.map((t) => (
            <option key={t.id} value={t.id}>
              {t.firstName} {t.lastName}
            </option>
          ))}
        </select>
        {eTrainer && <span className={styles.fieldError}>{eTrainer}</span>}
      </label>

      <label className={styles.label}>
        Član
        <select
          className={styles.input}
          value={memberId}
          onChange={(e) => setMemberId(e.target.value)}
        >
          <option value="">— izaberi člana —</option>
          {members.map((m) => (
            <option key={m.id} value={m.id}>
              {m.firstName} {m.lastName}
            </option>
          ))}
        </select>
        {eMember && <span className={styles.fieldError}>{eMember}</span>}
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

function ItemForm({ exercises, onAdded }) {
  const [exerciseId, setExerciseId] = useState("");
  const [sets, setSets] = useState(3);
  const [reps, setReps] = useState(10);
  const [weight, setWeight] = useState(0);

  const [eEx, setEEx] = useState("");
  const [eSets, setESets] = useState("");
  const [eReps, setEReps] = useState("");
  const [eW, setEW] = useState("");

  function validate() {
    let ok = true;
    setEEx("");
    setESets("");
    setEReps("");
    setEW("");
    if (!exerciseId) {
      setEEx("Vežba je obavezna.");
      ok = false;
    }
    if (Number(sets) < 1) {
      setESets("Serije ≥ 1.");
      ok = false;
    }
    if (Number(reps) < 1) {
      setEReps("Ponavljanja ≥ 1.");
      ok = false;
    }
    if (Number(weight) < 0) {
      setEW("Težina ≥ 0.");
      ok = false;
    }
    return ok;
  }

  async function onSubmit(e) {
    e.preventDefault();
    if (!validate()) return;
    await onAdded({
      exerciseId: Number(exerciseId),
      sets: Number(sets),
      reps: Number(reps),
      weight: Number(weight),
    });
    setExerciseId("");
    setSets(3);
    setReps(10);
    setWeight(0);
  }

  return (
    <form className={styles.formInline} onSubmit={onSubmit}>
      <select
        className={styles.input}
        value={exerciseId}
        onChange={(e) => setExerciseId(e.target.value)}
      >
        <option value="">— vežba —</option>
        {exercises.map((x) => (
          <option key={x.id} value={x.id}>
            {x.name}
          </option>
        ))}
      </select>
      <input
        className={styles.input}
        type="number"
        min="1"
        value={sets}
        onChange={(e) => setSets(e.target.value)}
        placeholder="Serije"
      />
      <input
        className={styles.input}
        type="number"
        min="1"
        value={reps}
        onChange={(e) => setReps(e.target.value)}
        placeholder="Ponavljanja"
      />
      <input
        className={styles.input}
        type="number"
        min="0"
        step="0.5"
        value={weight}
        onChange={(e) => setWeight(e.target.value)}
        placeholder="Težina (kg)"
      />
      <Button type="submit">Dodaj stavku</Button>

      {(eEx || eSets || eReps || eW) && (
        <div className={styles.inlineErrors}>
          {eEx && <span>{eEx}</span>}
          {eSets && <span>{eSets}</span>}
          {eReps && <span>{eReps}</span>}
          {eW && <span>{eW}</span>}
        </div>
      )}
    </form>
  );
}

export default function TrainingRecords() {
  const { addToast } = useToast();

  const [trainers, setTrainers] = useState([]);
  const [members, setMembers] = useState([]);
  const [exercises, setExercises] = useState([]);

  const [trainerFilter, setTrainerFilter] = useState("");
  const [memberFilter, setMemberFilter] = useState("");
  const [exerciseFilter, setExerciseFilter] = useState("");
  const [from, setFrom] = useState("");
  const [to, setTo] = useState("");

  const [records, setRecords] = useState([]);
  const [current, setCurrent] = useState(null);

  const [hasInteracted, setHasInteracted] = useState(false);
  const [editMode, setEditMode] = useState(false);

  const [editDate, setEditDate] = useState("");
  const [editTrainerId, setEditTrainerId] = useState("");
  const [editMemberId, setEditMemberId] = useState("");
  const [editItems, setEditItems] = useState([]);

  useEffect(() => {
    (async () => {
      try {
        const [ts, ms, xs] = await Promise.all([
          listTrainers(),
          listMembers(),
          listExercises(),
        ]);
        setTrainers(ts || []);
        setMembers(ms || []);
        setExercises(xs || []);
        const all = await searchRecords({
          trainerId: null,
          memberId: null,
          exerciseId: null,
          from: null,
          to: null,
        });
        setRecords(all);
      } catch {
        addToast("Ne mogu da učitam inicijalne podatke.", { duration: 3000 });
      }
    })();
  }, [addToast]);

  function validateDates() {
    if (from && to && from > to) {
      addToast("Datum 'Od' mora biti pre ili isti kao 'Do'.", {
        duration: 3000,
      });
      return false;
    }
    return true;
  }

  async function doSearch({ notify = true } = {}) {
    if (!validateDates()) return;
    try {
      const list = await searchRecords({
        trainerId: trainerFilter ? Number(trainerFilter) : null,
        memberId: memberFilter ? Number(memberFilter) : null,
        exerciseId: exerciseFilter ? Number(exerciseFilter) : null,
        from: from || null,
        to: to || null,
      });
      setRecords(list);
      if (notify) {
        addToast(
          list.length
            ? "Sistem je našao evidencije treninga po zadatim kriterijumima."
            : "Sistem ne može da nađe evidencije treninga po zadatim kriterijumima.",
          { duration: list.length ? 2000 : 3000 }
        );
      }
    } catch {
      if (notify)
        addToast(
          "Sistem ne može da nađe evidencije treninga po zadatim kriterijumima.",
          { duration: 3000 }
        );
    }
  }

  useEffect(() => {
    if (!hasInteracted) return;
    (async () => {
      await doSearch({ notify: true });
    })();
  }, [trainerFilter, memberFilter, exerciseFilter, from, to]);

  function onSubmitSearch(e) {
    e.preventDefault();
    setHasInteracted(true);
    doSearch({ notify: true });
  }

  async function handleCreateRecord() {
    setCurrent({});
    setEditMode(false);
    addToast("Sistem je kreirao evidenciju treninga.");
  }
  async function handleSaveHeader(payload) {
    try {
      const created = await createRecord(payload);
      setRecords((rs) => [created, ...rs]);
      setCurrent(created);
      addToast("Sistem je zapamtio evidenciju тренинга.");
    } catch {
      addToast("Sistem ne može da zapamti evidenciju treninga.", {
        duration: 3000,
      });
    }
  }

  async function handlePickRecord(r) {
    try {
      const full = await getRecord(r.id);
      setCurrent(full);
      setEditMode(false);
      setEditDate(full.trainingDate);
      setEditTrainerId(full.trainerId);
      setEditMemberId(full.memberId);
      setEditItems(
        (full.items || []).map((i) => ({
          exerciseId: i.exerciseId,
          exerciseName: i.exerciseName,
          sets: i.sets,
          reps: i.reps,
          weight: i.weight,
        }))
      );
      addToast("Sistem je našao evidenciju treninga.");
    } catch {
      addToast("Sistem ne može da nađe evidenciju treningа.", {
        duration: 3000,
      });
    }
  }

  function enterEdit() {
    if (current?.id) setEditMode(true);
  }
  function cancelEdit() {
    if (!current?.id) {
      setEditMode(false);
      return;
    }
    setEditDate(current.trainingDate);
    setEditTrainerId(current.trainerId);
    setEditMemberId(current.memberId);
    setEditItems(
      (current.items || []).map((i) => ({
        exerciseId: i.exerciseId,
        exerciseName: i.exerciseName,
        sets: i.sets,
        reps: i.reps,
        weight: i.weight,
      }))
    );
    setEditMode(false);
  }

  async function handleAddItemLocal({ exerciseId, sets, reps, weight }) {
    const ex = exercises.find((x) => x.id === exerciseId);
    setEditItems((items) => [
      ...items,
      {
        exerciseId,
        exerciseName: ex?.name || "",
        sets,
        reps,
        weight,
      },
    ]);
    addToast("Stavka dodata (nije sačuvano dok ne klikneš Sačuvaj).", {
      duration: 2500,
    });
  }
  function handleDeleteItemLocal(idx) {
    setEditItems((items) => items.filter((_, i) => i !== idx));
    addToast("Stavka uklonjena (nije sačuvano dok ne klikneš Sačuvaj).", {
      duration: 2500,
    });
  }

  async function handleSaveAll() {
    if (!current?.id) return;
    try {
      const payload = {
        date: editDate,
        trainerId: Number(editTrainerId),
        memberId: Number(editMemberId),
        items: editItems.map((i) => ({
          exerciseId: i.exerciseId,
          sets: Number(i.sets),
          reps: Number(i.reps),
          weight: Number(i.weight),
        })),
      };
      const updated = await updateRecord(current.id, payload);
      setCurrent(updated);
      setRecords((rs) => rs.map((r) => (r.id === updated.id ? updated : r)));
      setEditDate(updated.trainingDate);
      setEditTrainerId(updated.trainerId);
      setEditMemberId(updated.memberId);
      setEditItems(
        (updated.items || []).map((i) => ({
          exerciseId: i.exerciseId,
          exerciseName: i.exerciseName,
          sets: i.sets,
          reps: i.reps,
          weight: i.weight,
        }))
      );
      setEditMode(false);
      addToast("Sistem je zapamtio evidenciju treninga.");
    } catch {
      addToast("Sistem ne može da zapamti evidenciju treninga.", {
        duration: 3000,
      });
    }
  }

  async function handleDeleteRecord(rec) {
    if (!window.confirm("Da li ste sigurni da želite da obrišete evidenciju?"))
      return;
    try {
      await deleteRecord(rec.id);
      setRecords((rs) => rs.filter((r) => r.id !== rec.id));
      if (current?.id === rec.id) setCurrent(null);
      addToast("Evidencija treninga je obrisana.");
    } catch {
      addToast("Sistem ne može da obriše evidenciju treninga.", {
        duration: 3000,
      });
    }
  }

  const trainersById = useMemo(
    () => Object.fromEntries(trainers.map((t) => [t.id, t])),
    [trainers]
  );
  const membersById = useMemo(
    () => Object.fromEntries(members.map((m) => [m.id, m])),
    [members]
  );

  return (
    <section className={styles.page}>
      <Container>
        <div className={styles.topbar}>
          <form onSubmit={onSubmitSearch} className={styles.search}>
            <select
              className={styles.input}
              value={trainerFilter}
              onChange={(e) => {
                setTrainerFilter(e.target.value);
                setHasInteracted(true);
              }}
              title="Trener"
            >
              <option value="">Svi treneri</option>
              {trainers.map((t) => (
                <option key={t.id} value={t.id}>
                  {t.firstName} {t.lastName}
                </option>
              ))}
            </select>

            <select
              className={styles.input}
              value={memberFilter}
              onChange={(e) => {
                setMemberFilter(e.target.value);
                setHasInteracted(true);
              }}
              title="Član"
            >
              <option value="">Svi članovi</option>
              {members.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.firstName} {m.lastName}
                </option>
              ))}
            </select>

            <select
              className={styles.input}
              value={exerciseFilter}
              onChange={(e) => {
                setExerciseFilter(e.target.value);
                setHasInteracted(true);
              }}
              title="Vežba"
            >
              <option value="">Sve vežbe</option>
              {exercises.map((x) => (
                <option key={x.id} value={x.id}>
                  {x.name}
                </option>
              ))}
            </select>

            <input
              className={styles.input}
              type="date"
              value={from}
              onChange={(e) => {
                setFrom(e.target.value);
                setHasInteracted(true);
              }}
              placeholder="Od"
            />
            <input
              className={styles.input}
              type="date"
              value={to}
              onChange={(e) => {
                setTo(e.target.value);
                setHasInteracted(true);
              }}
              placeholder="Do"
            />

            <Button as="button" type="submit">
              Pretraži
            </Button>
            <Button
              as="button"
              type="button"
              variant="ghost"
              onClick={handleCreateRecord}
            >
              Kreiraj evidenciju
            </Button>
          </form>
        </div>

        <div className={styles.columns}>
          <Card title="Evidencije">
            <div className={styles.list}>
              {records.map((r) => (
                <div key={r.id} className={styles.rowItem}>
                  <div>
                    <div className={styles.name}>
                      {r.trainingDate} • Intenzitet:{" "}
                      {r.intensity?.toFixed?.(2) ?? r.intensity}
                    </div>
                    <div className={styles.meta}>
                      Trener:{" "}
                      {r.trainerName ||
                        (trainersById[r.trainerId] &&
                          `${trainersById[r.trainerId].firstName} ${
                            trainersById[r.trainerId].lastName
                          }`)}
                      {" • "}Član:{" "}
                      {r.memberName ||
                        (membersById[r.memberId] &&
                          `${membersById[r.memberId].firstName} ${
                            membersById[r.memberId].lastName
                          }`)}
                    </div>
                  </div>
                  <div className={styles.actionsInline}>
                    <Button variant="ghost" onClick={() => handlePickRecord(r)}>
                      Otvori
                    </Button>
                    <Button
                      variant="ghost"
                      onClick={() => handleDeleteRecord(r)}
                    >
                      Obriši
                    </Button>
                  </div>
                </div>
              ))}
              {records.length === 0 && (
                <div className={styles.empty}>Nema rezultata.</div>
              )}
            </div>
          </Card>

          <Card title={current?.id ? "Evidencija" : "Nova evidencija"}>
            {!current ? (
              <div className={styles.empty}>
                Izaberi evidenciju iz liste ili kreiraj novu.
              </div>
            ) : (
              <>
                {!current.id ? (
                  <RecordHeaderForm
                    initial={{}}
                    trainers={trainers}
                    members={members}
                    onCancel={() => setCurrent(null)}
                    onSaved={handleSaveHeader}
                    loading={false}
                  />
                ) : (
                  <>
                    {!editMode ? (
                      <div className={styles.box}>
                        <div className={styles.rowSpread}>
                          <div>
                            <div className={styles.name}>
                              {current.trainingDate} • Intenzitet:{" "}
                              {current.intensity?.toFixed?.(2) ??
                                current.intensity}
                            </div>
                            <div className={styles.meta}>
                              Trener: {current.trainerName} • Član:{" "}
                              {current.memberName}
                            </div>
                          </div>
                          <div className={styles.actionsInline}>
                            <Button variant="ghost" onClick={enterEdit}>
                              Izmeni
                            </Button>
                          </div>
                        </div>
                      </div>
                    ) : (
                      <>
                        <div className={styles.box}>
                          <div className={styles.formInline}>
                            <input
                              className={styles.input}
                              type="date"
                              value={editDate}
                              onChange={(e) => setEditDate(e.target.value)}
                            />
                            <select
                              className={styles.input}
                              value={editTrainerId}
                              onChange={(e) => setEditTrainerId(e.target.value)}
                            >
                              {trainers.map((t) => (
                                <option key={t.id} value={t.id}>
                                  {t.firstName} {t.lastName}
                                </option>
                              ))}
                            </select>
                            <select
                              className={styles.input}
                              value={editMemberId}
                              onChange={(e) => setEditMemberId(e.target.value)}
                            >
                              {members.map((m) => (
                                <option key={m.id} value={m.id}>
                                  {m.firstName} {m.lastName}
                                </option>
                              ))}
                            </select>
                          </div>
                        </div>

                        <h3 className={styles.subheading}>Stavke</h3>
                        <div className={styles.list}>
                          {editItems.map((it, idx) => (
                            <div key={idx} className={styles.rowItem}>
                              <div>
                                <div className={styles.name}>
                                  {idx + 1}. {it.exerciseName}
                                </div>
                                <div className={styles.meta}>
                                  {it.sets}×{it.reps} @ {it.weight} kg
                                </div>
                              </div>
                              <div className={styles.actionsInline}>
                                <Button
                                  variant="ghost"
                                  onClick={() => handleDeleteItemLocal(idx)}
                                >
                                  Obriši
                                </Button>
                              </div>
                            </div>
                          ))}
                          {editItems.length === 0 && (
                            <div className={styles.empty}>Još nema stavki.</div>
                          )}
                        </div>

                        <div className={styles.box}>
                          <ItemForm
                            exercises={exercises}
                            onAdded={handleAddItemLocal}
                          />
                        </div>

                        <div className={styles.row} style={{ marginTop: 12 }}>
                          <Button variant="ghost" onClick={cancelEdit}>
                            Otkaži
                          </Button>
                          <Button onClick={handleSaveAll}>Sačuvaj</Button>
                        </div>
                      </>
                    )}
                  </>
                )}
              </>
            )}
          </Card>
        </div>
      </Container>
    </section>
  );
}
