import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "./security/ProtectedRoute";
import Login from "./pages/Login/Login";
import Home from "./pages/Home/Home";

export default function RoutesDef() {
  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Home />
          </ProtectedRoute>
        }
      />
      <Route path="*" element={<Login />} />
    </Routes>
  );
}
