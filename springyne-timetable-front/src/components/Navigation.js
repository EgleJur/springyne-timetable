import { Link } from "react-router-dom";
import TeacherLessonToPdf from "../pages/TeacherLessonToPdf";
import { useState } from "react";

function Navigation() {

  const [open, setOpen] = useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <header className="bg-light">
      <div className="container-xxl">
        <nav className="navbar navbar-expand-lg bg-light">
          <div className="container-fluid">
            <button
              className="navbar-toggler"
              type="button"
              data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent"
              aria-expanded="false"
              aria-label="Toggle navigation"
            >
              <span className="navbar-toggler-icon"></span>
            </button>
            <div
              className="collapse navbar-collapse"
              id="navbarSupportedContent"
            >
              <div className="navbar-nav me-auto mb-2 mb-lg-0">
                <div className="nav-item">
                  <Link to="/" className="nav-link">
                    Tvarkaraščiai
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/modules" className="nav-link">
                    Moduliai
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/rooms" className="nav-link">
                    Kabinetai
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/shifts" className="nav-link">
                    Pamainos
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/subjects" className="nav-link">
                    Dalykai
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/teachers" className="nav-link">
                    Mokytojai
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/programs" className="nav-link">
                    Programos
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/groups" className="nav-link">
                    Grupės
                  </Link>
                </div>
                <div className="nav-item">
                  <Link to="/holidays" className="nav-link">
                    Atostogos
                  </Link>
                </div>

                <li className="nav-item dropdown">
                  <a className="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                    Ataskaitos
                  </a>
                  <ul className="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
                    <li><a className="dropdown-item" onClick={handleClickOpen}>Eksportuoti mokytojo tvarkaraštį į PDF</a></li>
                    <TeacherLessonToPdf
                      open={open}
                      handleClose={handleClose}
                      title="Eksportuoti mokytojo tvarkaraštį į PDF"
                    >
                      <p>Dialog content goes here</p>
                    </TeacherLessonToPdf>
                    <li><Link to="#" className="dropdown-item">Eksportuoti kabineto tvarkaraštį į PDF</Link></li>
                  </ul>
                </li>

              </div>
            </div>
          </div>
        </nav>

      </div>
    </header>
    // <div>
    //   {isDialogOpen && (
    //   <TeacherLessonToPdf
    //     title="Dialog title"
    //     message="Dialog message goes here"
    //     onClose={() => setIsDialogOpen(false)}
    //   />
    // )}
    // </div>
  );
}

export default Navigation;