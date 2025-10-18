import api from "./api";

export async function listCertificates() {
  const res = await api.get("/certificates");
  return res.data || [];
}

export async function createCertificate({ name, type }) {
  const res = await api.post("/certificates", { id: null, name, type });
  return res.data;
}

export async function updateCertificate(id, { name, type }) {
  const res = await api.put(`/certificates/${id}`, { id, name, type });
  return res.data;
}

export async function deleteCertificate(id) {
  await api.delete(`/certificates/${id}`);
}
