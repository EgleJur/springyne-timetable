import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { Collapse, Alert } from "@mui/material";

function ViewTeacherPage() {
  const [teacher, setTeacher] = useState({});
  const [deleted, setDeleted] = useState(false);
  const [restored, setRestored] = useState(false);
  const params = useParams();

  useEffect(() => {
    fetch("/api/v1/teachers/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
  }, [params.id]);

  const handleDelete = () => {
    fetch(`/api/v1/teachers/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
    setDeleted(true);
    setRestored(false);
  };

  const handleRestore = () => {
    fetch(`/api/v1/teachers/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setTeacher(jsonResponse));
    setRestored(true);
    setDeleted(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Mokytojas</h2>
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
              <th scope="col">Vardas ir Pavardė</th>
              <td>{teacher.name}</td>
            </tr>
            <tr>
              <th scope="col">Teams vardas(email)</th>
              <td>{teacher.teamsEmail}</td>
            </tr>
            <tr>
              <th scope="col">Kontaktinis email</th>
              <td>{teacher.email}</td>
            </tr>
            <tr>
              <th scope="col">Kontaktinis telefonas</th>
              <td>{teacher.phone}</td>
            </tr>
            <tr>
              <th scope="col">Valandų skaičius</th>
              <td>{teacher.hours}</td>
            </tr>
            <tr>
              <th scope="col">Dalykai</th>
              <td>
                {teacher.subjects?.map((subject) => (
                  <p key={subject.id} id={subject.id}>
                    {subject.name}
                  </p>
                ))}
              </td>
            </tr>
            <tr>
              <th scope="col">Pamaina</th>
              <td>{teacher.shift?.name}</td>
            </tr>

            <tr>
              <th scope="col">Būsena</th>
              <td>{teacher.deleted ? "Ištrintas" : "Aktyvus"}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas:</th>
              <td>{teacher.modifiedDate}</td>
            </tr>
          </tbody>
        </table>
        <button className="btn btn-primary me-2" disabled={teacher.deleted}>
          <Link to={"/teachers/edit/" + params.id} className="nav-link">
            Redaguoti
          </Link>
        </button>
        {teacher.deleted ? (
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

export default ViewTeacherPage;
