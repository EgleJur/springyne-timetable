import { Link } from "react-router-dom";

function Navigation() {
    return (
      <header className="bg-light">
        <div className="container-xxl">
          <nav className="navbar navbar-expand-lg bg-light">
            <div className="container-fluid">
              <button
                class="navbar-toggler"
                type="button"
                data-bs-toggle="collapse"
                data-bs-target="#navbarSupportedContent"
                aria-controls="navbarSupportedContent"
                aria-expanded="false"
                aria-label="Toggle navigation"
              >
                <span class="navbar-toggler-icon"></span>
              </button>
              <div
                className="collapse navbar-collapse"
                id="navbarSupportedContent"
              >
                <div className="navbar-nav me-auto mb-2 mb-lg-0">
                  <div className="nav-item">
                    <Link to="/" className="nav-link">
                      Pagrindinis
                    </Link>
                  </div>
                  <div className="nav-item">
                    <Link to="/modules" className="nav-link">
                      Moduliai
                    </Link>
                  </div>
                  <div className="nav-item">
                    <Link to="/rooms" className="nav-link">
                      Kambariai
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
                </div>
              </div>
            </div>
          </nav>
        </div>
      </header>
    );
}

export default Navigation;