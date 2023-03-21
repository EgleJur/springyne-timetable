import { HashRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import HomePage from "./pages/Home";
import Navigation from "./components/Navigation";
import ModuleListPage from "./pages/ModuleList";
import RoomListPage from "./pages/RoomList";
import ShiftListPage from "./pages/ShiftList";
import SubjectListPage from "./pages/SubjectList";
import TeacherListPage from "./pages/TeacherList";
import CreateModulePage from "./pages/CreateModule";
import ViewModulePage from "./pages/ViewModule";
import EditModulePage from "./pages/EditModule";
import CreateSubjectPage from "./pages/CreateSubject";
import ViewSubjectPage from "./pages/ViewSubject";
import EditSubjectPage from "./pages/EditSubject";
import CreateRoomPage from "./pages/CreateRoom";
import ViewRoomPage from "./pages/ViewRoom";
import EditRoomPage from "./pages/EditRoom";
import CreateShiftPage from "./pages/CreateShift";
import EditShiftPage from "./pages/EditShift";
import CreateTeacherPage from "./pages/CreateTeacher";
import EditTeacherPage from "./pages/EditTeacher";
import ViewTeacherPage from "./pages/ViewTeacher";
import GroupListPage from "./pages/GroupList";
import ProgramListPage from "./pages/ProgramList";
import CreateProgramPage from "./pages/CreateProgram";
import EditProgramPage from "./pages/EditProgram";
import ViewProgramPage from "./pages/ViewProgram";
import CreateGroupPage from "./pages/CreateGroup";
import EditGroupPage from "./pages/EditGroup";
import ViewGroupPage from "./pages/ViewGroup";
import HolidayListPage from "./pages/HolidayList";
import CreateHolidayPage from "./pages/CreateHoliday";
import ScheduleListPage from "./pages/ScheduleList";
import CreateSchedulePage from "./pages/CreateSchedule";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { lt } from "date-fns/locale";
import PlanSchedulePage from "./pages/PlanSchedule";
import Calendar from "./components/Calendar";

const apiUrl = process.env.PUBLIC_URL;
// const apiUrl = "http://localhost:8080";

function App() {
  return (
    <LocalizationProvider
      dateAdapter={AdapterDateFns}
      adapterLocale={lt}
      localeText={{
        previousMonth: "Ankstesnis mėnuo",
        nextMonth: "Kitas mėnuo",
      }}
    >
      <div className="App">
        <HashRouter>
          <Navigation />
          <div className="container-xxl">
            <Routes>
              <Route path="/" element={<ScheduleListPage />} />

              <Route path="modules" element={<ModuleListPage />} />
              <Route path="rooms" element={<RoomListPage />} />
              <Route path="shifts" element={<ShiftListPage />} />
              <Route path="shifts/create" element={<CreateShiftPage />} />
              <Route path="shifts/edit/:id" element={<EditShiftPage />} />
              <Route path="subjects" element={<SubjectListPage />} />
              <Route path="teachers" element={<TeacherListPage />} />
              <Route path="groups" element={<GroupListPage />} />
              <Route path="groups/view/:id" element={<ViewGroupPage />} />
              <Route path="teachers/create" element={<CreateTeacherPage />} />
              <Route path="teachers/edit/:id" element={<EditTeacherPage />} />
              <Route path="teachers/view/:id" element={<ViewTeacherPage />} />
              <Route path="modules/create" element={<CreateModulePage />} />
              <Route path="modules/view/:id" element={<ViewModulePage />} />
              <Route path="modules/edit/:id" element={<EditModulePage />} />
              <Route path="subjects/create" element={<CreateSubjectPage />} />
              <Route path="subjects/view/:id" element={<ViewSubjectPage />} />
              <Route path="subjects/edit/:id" element={<EditSubjectPage />} />
              <Route path="rooms/create" element={<CreateRoomPage />} />
              <Route path="rooms/view/:id" element={<ViewRoomPage />} />
              <Route path="rooms/edit/:id" element={<EditRoomPage />} />
              <Route path="programs" element={<ProgramListPage />} />
              <Route path="programs/create" element={<CreateProgramPage />} />
              <Route path="programs/edit/:id" element={<EditProgramPage />} />
              <Route path="programs/view/:id" element={<ViewProgramPage />} />
              <Route path="groups/create" element={<CreateGroupPage />} />
              <Route path="groups/edit/:id" element={<EditGroupPage />} />
              <Route path="groups/view/:id" element={<ViewGroupPage />} />
              <Route path="holidays" element={<HolidayListPage />} />
              <Route path="holidays/create" element={<CreateHolidayPage />} />
              {/* <Route path="schedules" element={<ScheduleListPage />} /> */}
              <Route path="schedules/plan/:id" element={<PlanSchedulePage />} />
              <Route path="schedules/create" element={<CreateSchedulePage />} />
              <Route path="calendar" element={<Calendar />} />
            </Routes>
          </div>
        </HashRouter>
      </div>
    </LocalizationProvider>
  );
}

export default App;
export { apiUrl };
