import { HashRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import HomePage from './pages/Home';
import Navigation from './components/Navigation';
import Teacher from './pages/Teacher';

function App() {
  return (
    <div className="App">
      <HashRouter>
        <Navigation/>

        <Routes>
          <Route path='/' element={<HomePage/>}/>
          <Route path='/teacher' element={<Teacher />} />
        </Routes>
      </HashRouter>
    </div>
  );
}

export default App;
