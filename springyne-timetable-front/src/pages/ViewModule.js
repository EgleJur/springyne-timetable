import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";

function ViewModulePage() {
  const [module, setModule] = useState({});
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();
  const [subjects, setSubjects] = useState([]);

  useEffect(() => {
    fetch("/api/v1/modules/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
  }, [params.id]);

  useEffect(() => {
    fetch("/api/v1/modules/subjects/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setSubjects(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`/api/v1/modules/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
    setDeleted(true);
    setRestored(false);
  };

  const handleRestore = () => {
    fetch(`/api/v1/modules/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setModule(jsonResponse));
    setRestored(true);
    setDeleted(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Modulis</h2>
      <Collapse in={deleted}>
        <Alert
          onClose={() => {
            setDeleted(false);
          }}
          severity="info"
          className="mb-3"
        >
          Įrašas sėkmingai ištrintas
        </Alert>
      </Collapse>

      <Collapse in={restored}>
        <Alert
          onClose={() => {
            setRestored(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atstatytas
        </Alert>
      </Collapse>
      <div className="">
        <table className="table table-hover shadow p-3 mb-5 bg-body rounded align-middle">
          <tbody>
            <tr>
              <th scope="col">Numeris</th>
              <td colSpan={4}>{module.number}</td>
            </tr>
            <tr>
              <th scope="col">Pavadinimas</th>
              <td colSpan={4}>{module.name}</td>
            </tr>
            <tr>
              <th scope="col">Būsena</th>
              <td colSpan={4}>{module.deleted ? "Modulis ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas</th>
              <td colSpan={4}>{module.modifiedDate}</td>
            </tr>
            {subjects.length > 0 ? (
              <tr>
                <th rowSpan="0">Dalykai</th>
              </tr>
            ) : (
              ""
            )}
            {subjects?.map((subject) => (
              <tr
                className={subject.deleted ? "text-black-50" : ""}
                key={subject.id}
              >
                <td>{subject.name}</td>
                {/* <td>{subject.description}</td>
                <td>{subject.last_Updated}</td> */}
                {subject.deleted ? <td>Dalykas ištrintas</td> : <td></td>}
              </tr>
              
            ))}
          </tbody>
        </table>
        <button className="btn btn-primary me-2" disabled={module.deleted}>
          <Link to={"/modules/edit/" + params.id} className="nav-link">
            Redaguoti
          </Link>
        </button>
        {module.deleted ? (
          <button className="btn btn-secondary me-2" onClick={handleRestore}>
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </div>
    </div>
  );
}

export default ViewModulePage;
