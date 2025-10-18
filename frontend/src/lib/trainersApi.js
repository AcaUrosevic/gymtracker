import api from "./api";

export async function listTrainers() {
  const res = await api.get("/trainers");
  return res.data || [];
}
