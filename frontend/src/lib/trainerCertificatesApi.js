import api from "./api";

export async function listTrainerCertificates(trainerId) {
  const res = await api.get(`/trainers/${trainerId}/certificates`);
  return res.data || [];
}

export async function assignTrainerCertificate(
  trainerId,
  { certificateId, issuedAt }
) {
  const res = await api.post(`/trainers/${trainerId}/certificates`, {
    certificateId,
    issuedAt: issuedAt || null,
  });
  return res.data;
}

export async function revokeTrainerCertificate(trainerId, certificateId) {
  await api.delete(`/trainers/${trainerId}/certificates/${certificateId}`);
}

export async function listTrainersWithCertificates() {
  const res = await api.get(`/trainers/with-certificates`);
  return res.data || [];
}
