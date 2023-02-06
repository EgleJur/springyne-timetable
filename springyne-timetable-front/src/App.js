import { HashRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import HomePage from './pages/Home';
import Navigation from './components/Navigation';

function App() {
  return (
    <div className="App">
      <HashRouter>
        <Navigation/>

        <Routes>
          <Route path='/' element={<HomePage/>}/>
        </Routes>
      </HashRouter>
    </div>
  );
}

export default App;
