import { HashRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import HomePage from './pages/Home';
import Navigation from './components/Navigation';
import Teacher from './pages/Teacher';
import AddTeacher from './pages/teacher_pages/AddTeacher';
import EditTeacher from './pages/teacher_pages/EditTeacher';
import ViewTeacher from './pages/teacher_pages/ViewTeacher';




function App() {
  return (
    <div className="App">
      <HashRouter>
        <Navigation/>

        <Routes>
          <Route path='/' element={<HomePage/>}/>
          <Route path='/teacher' element={<Teacher />} />
          <Route path='/addteacher' element={<AddTeacher />} />
          <Route path='/teacher_pages/editteacher/:id' element={<EditTeacher />} />
          <Route path='/teacher_pages/viewteacher/:id' element={<ViewTeacher />} />
          
        </Routes>
      </HashRouter>
    </div>
  );
}

export default App;
