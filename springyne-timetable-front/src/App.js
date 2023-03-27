import { HashRouter, Route, Routes } from "react-router-dom";
import "./App.css";
import Navigation from "./components/Navigation";
import ModuleListPage from "./pages/module/ModuleList";
import RoomListPage from "./pages/room/RoomList";
import ShiftListPage from "./pages/shift/ShiftList";
import SubjectListPage from "./pages/subject/SubjectList";
import TeacherListPage from "./pages/teacher/TeacherList";
import CreateModulePage from "./pages/module/CreateModule";
import ViewModulePage from "./pages/module/ViewModule";
import EditModulePage from "./pages/module/EditModule";
import CreateSubjectPage from "./pages/subject/CreateSubject";
import ViewSubjectPage from "./pages/subject/ViewSubject";
import EditSubjectPage from "./pages/subject/EditSubject";
import CreateRoomPage from "./pages/room/CreateRoom";
import ViewRoomPage from "./pages/room/ViewRoom";
import EditRoomPage from "./pages/room/EditRoom";
import CreateShiftPage from "./pages/shift/CreateShift";
import EditShiftPage from "./pages/shift/EditShift";
import CreateTeacherPage from "./pages/teacher/CreateTeacher";
import EditTeacherPage from "./pages/teacher/EditTeacher";
import ViewTeacherPage from "./pages/teacher/ViewTeacher";
import GroupListPage from "./pages/group/GroupList";
import ProgramListPage from "./pages/program/ProgramList";
import CreateProgramPage from "./pages/program/CreateProgram";
import EditProgramPage from "./pages/program/EditProgram";
import ViewProgramPage from "./pages/program/ViewProgram";
import CreateGroupPage from "./pages/group/CreateGroup";
import EditGroupPage from "./pages/group/EditGroup";
import ViewGroupPage from "./pages/group/ViewGroup";
import HolidayListPage from "./pages/holiday/HolidayList";
import CreateHolidayPage from "./pages/holiday/CreateHoliday";
import ScheduleListPage from "./pages/schedule/ScheduleList";
import CreateSchedulePage from "./pages/schedule/CreateSchedule";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDateFns } from "@mui/x-date-pickers/AdapterDateFns";
import { lt } from "date-fns/locale";
import PlanSchedulePage from "./pages/schedule/PlanSchedule";
import Calendar from "./calendar/Calendar";


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
