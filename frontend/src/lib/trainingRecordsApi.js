import api from "./api";

export async function listTrainers() {
  const res = await api.get("/trainers");
  return res.data || [];
}
export async function listMembers() {
  const res = await api.get("/members");
  return res.data || [];
}
export async function listExercises() {
  const res = await api.get("/exercises");
  return res.data || [];
}

export async function searchRecords({
  trainerId,
  memberId,
  exerciseId,
  from,
  to,
}) {
  const hasAny = !!trainerId || !!memberId || !!exerciseId || !!from || !!to;

  if (!hasAny) {
    const res = await api.get("/training-records");
    return res.data || [];
  }

  const params = new URLSearchParams();
  if (trainerId) params.set("trainerId", String(trainerId));
  if (memberId) params.set("memberId", String(memberId));
  if (exerciseId) params.set("exerciseId", String(exerciseId));
  if (from) params.set("from", from);
  if (to) params.set("to", to);

  const res = await api.get(`/training-records/search?${params.toString()}`);
  return res.data || [];
}

export async function createRecord({ date, trainerId, memberId }) {
  const res = await api.post("/training-records", {
    date,
    trainerId,
    memberId,
  });
  return res.data;
}
export async function updateRecord(id, { date, trainerId, memberId, items }) {
  const res = await api.put(`/training-records/${id}`, {
    date,
    trainerId,
    memberId,
    items,
  });
  return res.data;
}
export async function getRecord(id) {
  const res = await api.get(`/training-records/${id}`);
  return res.data;
}
export async function deleteRecord(id) {
  await api.delete(`/training-records/${id}`);
}

export async function addRecordItem(
  recordId,
  { exerciseId, sets, reps, weight }
) {
  const res = await api.post(`/training-records/${recordId}/items`, {
    exerciseId,
    sets,
    reps,
    weight,
  });
  return res.data;
}
export async function deleteRecordItem(recordId, rb) {
  await api.delete(`/training-records/${recordId}/items/${rb}`);
}
