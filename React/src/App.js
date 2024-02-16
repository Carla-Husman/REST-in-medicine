import { Routes, Route, Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import Client from "./pages/ClientPage";
import MakeAppointemntPage from "./pages/MakeAppointmentPage";
import PhysicianPage from "./pages/PhysicianPage";
import ConsultationPage from "./pages/ConsultationPage";
import UpdateInfoPage from "./pages/UpdateInfoPage";
import UpdateInfoPatientPhysicianPage from "./pages/UpdateInfoPatientPhysicianPage";

// Define the routes for the application using React Router's Routes component
function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Navigate to='/login' />} />
        <Route path="login" element={<LoginPage />} />
        <Route path="client/:id" element={<Client />} />
        <Route path="update" element={<UpdateInfoPage />} />
        <Route path="update/:type" element={<UpdateInfoPatientPhysicianPage />} />
        <Route path="physician/:id" element={<PhysicianPage />} />
        <Route path="noPages" element={<h1>404 This is not the web page you are looking for</h1>} />
        <Route path="register" element={<RegisterPage />} />
        <Route path="makeappointment/:physicianId" element={<MakeAppointemntPage />} />
        <Route path="consultation/:patientId" element={<ConsultationPage />} />
        <Route path="*" element={<Navigate to='/noPages' />} />
      </Routes>
    </>
  );
}

export default App;