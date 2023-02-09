import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

function ModuleListPage() {
  const [modules, setModules] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [searchName, setSearchName] = useState("");

  useEffect(() => {
    fetch(`/api/v1/modules`)
      .then((response) => response.json())
      .then((jsonResponse) => setModules(jsonResponse));
  }, []);

  const searchAndPage = () => {
    let newPageNumber = pageNumber;
    if (newPageNumber === "") {
      newPageNumber = 0;
      setPageNumber(0);
    }
    if (pageSize === "") {
      setPageSize(20);
    }
    fetch(
      `/api/v1/modules/search?name=${searchName}&page=${newPageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => setModules(jsonResponse));
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Moduliai</h2>
      <div className="d-flex">
        <button className="btn btn-primary mb-5">
          <Link to="/modules/create" className="nav-link">
            Pridėti naują modulį
          </Link>
        </button>
      </div>
      <div className="d-flex justify-content-end">
        <div className="mb-4">
          <form className="d-flex" role="search">
            <input
              className="form-control me-2 w-25"
              id="input-pageNumber"
              placeholder="Puslapis"
              value={pageNumber}
              onChange={(e) => setPageNumber(e.target.value)}
            />
            <input
              className="form-control me-2 w-25"
              id="input-pageSize"
              placeholder="Puslapyje"
              value={pageSize}
              onChange={(e) => setPageSize(e.target.value)}
            />
            <input
              className="form-control me-2"
              type="search"
              placeholder="Ieškoti pavadinimo"
              aria-label="Search"
              id="input-searchName"
              value={searchName}
              onChange={(e) => setSearchName(e.target.value)}
            />
            <button
              className="btn btn-outline-primary"
              type="submit"
              onClick={searchAndPage}
            >
              Ieškoti
            </button>
          </form>
        </div>
      </div>

      <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
        <thead className="table-light">
          <tr>
            <th>Numeris</th>
            <th>Pavadinimas</th>
            <th>Detalės</th>
            <th>Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {modules.map((module) => (
            <tr key={module.id} id={module.id}>
              <td>{module.number}</td>
              <td>{module.name}</td>
              <td>{module.deleted ? "Modulis ištrintas" : ""}</td>
              <td>
                <button className="btn btn-outline-primary">Žiūrėti</button>
                <button className="btn btn-outline-primary ms-2">
                  Redaguoti
                </button>
                {module.deleted ? (
                  <button className="btn btn-outline-danger ms-2">
                    Atstatyti
                  </button>
                ) : (
                  <button className="btn btn-outline-danger ms-2">
                    Ištrinti
                  </button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default ModuleListPage;
