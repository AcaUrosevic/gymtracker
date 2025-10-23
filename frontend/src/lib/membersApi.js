import api from "./api";

export async function createMember(payload) {
  const res = await api.post("/members", payload);
  return res.data;
}

export async function updateMember(id, payload) {
  const res = await api.put(`/members/${id}`, payload);
  return res.data;
}

export async function deleteMember(id) {
  await api.delete(`/members/${id}`);
}

export async function listPackages() {
  const res = await api.get("/packages");
  return res.data;
}

export async function searchMembers(q = "", packageId = null) {
  const params = new URLSearchParams();
  const s = q.trim();
  if (s) params.set("q", s);
  if (packageId) params.set("packageId", String(packageId));
  const url = `/members/search${
    params.toString() ? `?${params.toString()}` : ""
  }`;
  const res = await api.get(url);
  return res.data || [];
}
