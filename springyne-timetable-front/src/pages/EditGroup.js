import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import {
  TextField,
  FormControl,
  Select,
  MenuItem,
  InputLabel,
  OutlinedInput,
} from "@mui/material";
import { apiUrl } from "../App";

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
  const params = useParams();
  const [showProgramMenuItem, setShowProgramMenuItem] = useState(true);
  const [showShiftMenuItem, setShowShiftMenuItem] = useState(true);

  const fetchGroup = () => {
    fetch(`${apiUrl}/api/v1/groups/` + params.id)
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
  };

  useEffect(() => fetchGroup, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/programs/`)
      .then((response) => response.json())
      .then(setPrograms);
  }, []);

  useEffect(() => {
    fetch(`${apiUrl}/api/v1/shifts/`)
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
        `${apiUrl}/api/v1/groups/edit/${params.id}?programId=${selectedProgram}&shiftId=${selectedShift}`,
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
          fetchGroup();
          setTimeout(() => {
            setSuccess(false);
          }, 5000);
        } else {
          setFailure(true);
          setSuccess(false);
          setTimeout(() => {
            setFailure(false);
          }, 5000);
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
    fetch(`${apiUrl}/api/v1/groups/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setTimeout(() => {
      setSuccess(false);
             }, 5000);
  };
  const handleRestore = () => {
    fetch(`${apiUrl}/api/v1/groups/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setGroup(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setTimeout(() => {
      setSuccess(false);
             }, 5000);
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
      <div className="container-fluid shadow p-3 mb-4 mb-md-5 bg-body rounded">
        <form noValidate>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-group-name-with-error">Pavadinimas *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!nameError}
                onChange={(e) => updateProperty("name", e)}
                value={group.name}
                id="edit-group-name-with-error"
                label=""
                helperText="Pavadinimas privalomas"
                className="form-control mb-3"
                size="small"
                disabled={group.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-group-group-year">Mokslo metai *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!groupYearError}
                onChange={(e) => updateProperty("groupYear", e)}
                value={group.groupYear}
                id="edit-group-group-year"
                label=""
                helperText="Mokslo metai privalomi"
                className="form-control mb-3"
                size="small"
                disabled={group.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-group-students">Studentų skaičius *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
              <TextField
                error={!!studentsError}
                onChange={(e) => updateProperty("students", e)}
                value={group.students}
                id="edit-group-students"
                label=""
                helperText="Studentų skaičius privalomas"
                className="form-control mb-2"
                size="small"
                disabled={group.deleted}
                InputLabelProps={{ shrink: true }}
                required
              />
            </div>
          </div>

          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="add-program">Programa *</label>
            </div>
            <div className="col-md-8 mb-2">
              <FormControl fullWidth size="small" className="mb-3">
                {/* <InputLabel id="select-module-label">
                  {group.program?.name}
                </InputLabel> */}
                <Select
                  labelId="select-program-label"
                  id="select-program"
                  fullWidth
                  value={selectedProgram}
                  disabled={group.deleted}
                  onChange={(e) => setSelectedProgram(e.target.value)}
                  displayEmpty
                  onOpen={() => {
                    setShowProgramMenuItem(false);
                  }}
                  onClose={() => {
                    setShowProgramMenuItem(true);
                  }}
                >
                  <MenuItem
                    value=""
                    style={{ display: showProgramMenuItem ? "block" : "none" }}
                  >
                    {group.program?.name}
                  </MenuItem>
                  {programs?.map((program) => (
                    <MenuItem
                      value={program.id}
                      key={program.id}
                      disabled={program.deleted}
                    >
                      {program.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </div>
          </div>

          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-shift">Pamaina *</label>
            </div>
            <div className="col-md-8 mb-2">
              <FormControl fullWidth size="small" className="mb-3">
                {/* <InputLabel id="select-module-label">
                  {group.shift?.name}
                </InputLabel> */}
                <Select
                  disabled={group.deleted}
                  labelId="select-shift-label"
                  id="select-shift"
                  input={<OutlinedInput notched label="" />}
                  fullWidth
                  value={selectedShift}
                  onChange={(e) => setSelectedShift(e.target.value)}
                  displayEmpty
                  onOpen={() => {
                    setShowShiftMenuItem(false);
                  }}
                  onClose={() => {
                    setShowShiftMenuItem(true);
                  }}
                >
                  <MenuItem
                    value=""
                    style={{ display: showShiftMenuItem ? "block" : "none" }}
                  >
                    {group.shift?.name}
                  </MenuItem>
                  {shifts?.map((shift) => (
                    <MenuItem value={shift.id} key={shift.id}>
                      {shift.name}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </div>
          </div>

          <div className="row mb-md-4">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">Būsena</div>
            <div className="col-md-8 mb-2 mb-md-0">
              {group.deleted ? "Ištrintas" : "Aktyvus"}
            </div>
          </div>
          <div className="row mb-md-4">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              Paskutinį kartą modifikuotas
            </div>
            <div className="col-md-8 mb-2 mb-md-0">{group.modifiedDate}</div>
          </div>
        </form>
      </div>

{group.deleted ? (
      <div>
        <button
          type="submit"
          className="btn btn-primary me-2 mt-2 mb-5"
          onClick={editGroup}
          disabled
        >
          Redaguoti
        </button>
         
          <button
            className="btn btn-secondary me-2 mt-2 mb-5"
            onClick={handleRestore}
          >
            Atstatyti
          </button>
          </div>
        ) : (
          <div>
          <button
          type="submit"
          className="btn btn-primary me-2 mt-2 mb-5"
          onClick={editGroup}
          
        >
          Redaguoti
        </button>
          <button
            className="btn btn-danger me-2 mt-2 mb-5"
            onClick={handleDelete}
          >
            Ištrinti
          </button> </div>
        )}
     
    </div>
  );
}

export default EditGroupPage;
