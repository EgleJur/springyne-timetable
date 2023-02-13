import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
// import { useParams } from "react-router-dom";
// import { useHref } from 'react-router-dom';

function SubjectListPage() {
  const [subjects, seSubjects] = useState([]);
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(20);
  const [searchName, setSearchName] = useState("");
  // const listUrl = useHref('/');
  // const [error, setError] = useState();
  // const params = useParams();
  const JSON_HEADERS = {
    "Content-Type": "application/json"
}
  const fetchSubjects = () => {
    fetch(`/api/v1/subject`)
    .then((response) => response.json())
    .then((jsonResponse) => seSubjects(jsonResponse));

  };

  useEffect(() => {
    fetchSubjects();
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
      `/api/v1/subject/search?name=${searchName}&page=${newPageNumber}&pageSize=${pageSize}`
    )
      .then((response) => response.json())
      .then((jsonResponse) => seSubjects(jsonResponse));
  };

  const deleteSubject = (id) => {
    fetch('/api/v1/subject/delete/'+ id,{ 
      method: 'PATCH',
      headers: JSON_HEADERS,
    })
    .then(fetchSubjects);
  };

  const restoreSubject = (id) => {
    fetch('/api/v1/subject/restore/'+ id,{ 
      method: 'PATCH',
      headers: JSON_HEADERS,
    })
    .then(fetchSubjects);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Dalykai</h2>
      <div className="d-flex">
        <button className="btn btn-primary mb-5">
          <Link to="/subjects/create" className="nav-link">
            Pridėti naują dalyą
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
            <th>Pavadinimas</th>
            <th>Modulis</th>
            <th>Detalės</th>
            <th>Veiksmai</th>
          </tr>
        </thead>
        <tbody>
          {subjects.map((subject) => (
            <tr key={subject.id} id={subject.id}>
              <td>{subject.name}</td>
              <td>{subject.module.map((mod)=>(
                <p key={mod.id} id={mod.id}>{mod.name}</p>
              ))}</td>
              <td>{subject.deleted ? "Modulis ištrintas" : ""}</td>
              <td>
                <button className="btn btn-outline-primary">Žiūrėti</button>
                <button className="btn btn-outline-primary ms-2">
                  Redaguoti
                </button>
                {subject.deleted ? (
                  <button className="btn btn-outline-danger ms-2" onClick={()=>restoreSubject(subject.id)}>
                    Atstatyti
                  </button>
                ) : (
                  
                  <button className="btn btn-outline-danger ms-2" onClick={()=>deleteSubject(subject.id)}>
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

export default SubjectListPage;