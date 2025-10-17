import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./security/ProtectedRoute";
import Login from "./pages/Login/Login";
import Home from "./pages/Home/Home";
import Members from "./pages/Members/Members";
import TrainingRecords from "./pages/TrainingRecords/TrainingRecords";
import Layout from "./components/Layout/Layout";

export default function RoutesDef() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />

      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Layout>
              <Home />
            </Layout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/members"
        element={
          <ProtectedRoute>
            <Layout>
              <Members />
            </Layout>
          </ProtectedRoute>
        }
      />

      <Route
        path="/training-records"
        element={
          <ProtectedRoute>
            <Layout>
              <TrainingRecords />
            </Layout>
          </ProtectedRoute>
        }
      />

      <Route
        path="*"
        element={
          <ProtectedRoute>
            <Layout>
              <Home />
            </Layout>
          </ProtectedRoute>
        }
      />
    </Routes>
  );
}
