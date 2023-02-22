import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { Link, useParams } from "react-router-dom";
import { TextField } from "@mui/material";

function EditGroupPage() {
  const [group, setGroup] = useState({});
  const [nameError, setNameError] = useState(false);
  const [groupYearError, setGroupYearError] = useState(false);
  const [studentsError, setStudentsError] = useState(false);
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [programs, setPrograms] = useState([]);
  const [shifts, setShifts] = useState([]);
  const [selectedProgram, setSelectedProgram] = useState("");
  const [selectedShift, setSelectedShift] = useState("");
  const [changed, setChanged] = useState(false);
  const params = useParams();

  const fetchGroup = () => {
    fetch("/api/v1/groups/" + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
  };

  useEffect(() => fetchGroup, []);

  useEffect(() => {
    fetch("api/v1/programs/")
      .then((response) => response.json())
      .then(setPrograms);
  }, []);

  useEffect(() => {
    fetch("api/v1/shifts/")
      .then((response) => response.json())
      .then(setShifts);
  }, []);

  const editGroup = (e) => {
    e.preventDefault();
    setNameError(false);
    setGroupYearError(false);
    setStudentsError(false);
    if (group.name === "" || group.groupYear === "" || group.students === "") {
      if (group.name === "") {
        setNameError(true);
      }
      if (group.groupYear === "") {
        setGroupYearError(true);
      }
      if (group.students === "") {
        setStudentsError(true);
      }
    } else {
      fetch(
        `api/v1/groups/edit/${params.id}?programId=${selectedProgram}&shiftId=${selectedShift}`,
        {
          method: "PATCH",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(group),
        }
      ).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          setChanged(false);
          fetchGroup();
        } else {
          setFailure(true);
          setSuccess(false);
          setNameError(true);
          setGroupYearError(false);
          setStudentsError(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setGroup({
      ...group,
      [property]: event.target.value,
    });
  };

  const handleDelete = () => {
    fetch(`/api/v1/groups/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setGroupYearError(false);
    setStudentsError(false);
    setChanged(false);
  };

  const handleRestore = () => {
    fetch(`/api/v1/groups/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setGroupYearError(false);
    setStudentsError(false);
    setChanged(false);
  };

  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti grupę</h2>
      <Collapse in={success}>
        <Alert
          onClose={() => {
            setSuccess(false);
          }}
          severity="success"
          className="mb-3"
        >
          Įrašas sėkmingai atnaujintas
        </Alert>
      </Collapse>
      <Collapse in={failure}>
        <Alert
          onClose={() => {
            setFailure(false);
          }}
          severity="error"
          className="mb-3"
        >
          Įrašo nepavyko atnaujinti
        </Alert>
      </Collapse>
      <form noValidate>
        <table
          className="table table-hover shadow p-3 mb-5 bg-body rounded
        align-middle"
        >
          <tbody>
            <tr>
              <th scope="col">
                <label htmlFor="edit-room-name-with-error">Pavadinimas *</label>
              </th>
              <td>
                <TextField
                  error={!!nameError}
                  onChange={(e) => updateProperty("name", e)}
                  value={group.name}
                  id="group-name-with-error"
                  helperText="Pavadinimas negali būti tuščias laukas"
                  className="form-control mb-3"
                  size="small"
                  disabled={group.deleted}
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>
            <tr>
              <th scope="col">
                <label htmlFor="edit-room-building-with-error">Metai *</label>
              </th>
              <td>
                <TextField
                  error={!!groupYearError}
                  onChange={(e) => updateProperty("groupYear", e)}
                  value={group.groupYear}
                  id="create-group-group-year"
                  helperText="Metai negali būti tuščias laukas"
                  className="form-control mb-3"
                  size="small"
                  disabled={group.deleted}
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>

            <tr>
              <th scope="col">
                <label htmlFor="edit-room-description">Studentai *</label>
              </th>
              <td>
                <TextField
                  error={!!studentsError}
                  onChange={(e) => updateProperty("students", e)}
                  value={group.students}
                  id="create-group-students"
                  helperText="Studentai negali būti tuščias laukas"
                  className="form-control mb-2"
                  size="small"
                  disabled={group.deleted}
                  InputLabelProps={{ shrink: true }}
                  required
                />
              </td>
            </tr>

            <tr>
              <th scope="col">
                <label htmlFor="edit-group-description">Programa</label>
              </th>
              <td>
                <select
                  value={selectedProgram}
                  onChange={(e) => setSelectedProgram(e.target.value)}
                  disabled={group.deleted}
                  className="form-control mb-3"
                >
                  <option value="" disabled>
                    {group.program?.name}
                  </option>
                  {programs.map((program) => (
                    <option
                      key={program.id}
                      value={program.id}
                      disabled={program.deleted}
                    >
                      {program.name}
                    </option>
                  ))}
                </select>
              </td>
            </tr>

            <tr>
              <th scope="col">
                <label htmlFor="edit-group-description">Pamaina</label>
              </th>
              <td>
                <select
                  value={selectedShift}
                  onChange={(e) => setSelectedShift(e.target.value)}
                  disabled={group.deleted}
                  className="form-control mb-3"
                >
                  <option value="" disabled>
                    {group.shift?.name}
                  </option>
                  {shifts.map((shift) => (
                    <option
                      key={shift.id}
                      value={shift.id}
                      disabled={shift.deleted}
                    >
                      {shift.name}
                    </option>
                  ))}
                </select>
              </td>
            </tr>

            <tr>
              <th scope="col">Būsena</th>
              <td>{group.deleted ? "Ištrinta" : ""}</td>
            </tr>
            <tr>
              <th scope="col">Paskutinį kartą modifikuotas:</th>
              <td>{group.modifiedDate}</td>
            </tr>
          </tbody>
        </table>
        <button
          type="submit"
          className="btn btn-primary me-2"
          onClick={editGroup}
          disabled={!changed}
        >
          Redaguoti
        </button>

        {group.deleted ? (
          <button className="btn btn-secondary me-2" onClick={handleRestore}>
            Atstatyti
          </button>
        ) : (
          <button className="btn btn-danger me-2" onClick={handleDelete}>
            Ištrinti
          </button>
        )}
      </form>
    </div>
  );
}

export default EditGroupPage;
