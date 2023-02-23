import { Collapse, Alert } from "@mui/material";
import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { TextField } from "@mui/material";



function EditSubjectPage() {
  const [subject, setSubject] = useState({});
  const [nameError, setNameError] = useState("");
  const [module, setModule] = useState({});
  const [room, setRoom] = useState("");
  const [success, setSuccess] = useState(false);
  const [failure, setFailure] = useState(false);
  const [changed, setChanged] = useState(false);
  const params = useParams();
  const [rooms, setRooms] = useState([]);
  const [selectedRoom, setSelectedRoom] = useState("");
  const [selectedModule, setSelectedModule] = useState('');
  const [modules, setModules] = useState([]);

  const fetchSubject =()=>{
    fetch("/api/v1/subjects/" + params.id)
  .then((response) => response.json())
  .then((jsonResponse) => setSubject(jsonResponse));
};
  
  useEffect(() => fetchSubject, [params.id]);

  useEffect(() => {
    fetch('api/v1/rooms/')
      .then(response => response.json())
      .then(setRooms)

  }, []);
  
  useEffect(() => {
    fetch('api/v1/modules/')
      .then(response => response.json())
      .then(setModules)

  }, []);
  
  const deleteRoom  = (e) => {
    fetch(`/api/v1/subjects/${params.id}/deleteRoom/${e}`, {
      method: "PATCH",
    }).then(fetchSubject)
  };
  const addRoom  = (e) => {
    fetch(`/api/v1/subjects/${params.id}/addRoom/${e}`, {
      method: "PATCH",
    }).then(fetchSubject)
  };

  const editsubject = (e) => {
    e.preventDefault();
    setNameError(false);
    if (subject.name === "") {
      setNameError(true);
    } else {
      fetch(`api/v1/subjects/edit/${params.id}?moduleId=${selectedModule}&roomId=${selectedRoom}`, {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(subject),
      }).then((result) => {
        if (result.ok) {
          setSuccess(true);
          setFailure(false);
          setChanged(false);
          fetchSubject();
        } else {
          setFailure(true);
          setSuccess(false);
        }
      });
    }
  };
  const updateProperty = (property, event) => {
    setSubject({
      ...subject,
      [property]: event.target.value,
    });
    setChanged(true);
  };

  const handleDelete = () => {
    fetch(`/api/v1/subjects/delete/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setChanged(false);
  };
  const handleRestore = () => {
    fetch(`/api/v1/subjects/restore/` + params.id, {
      method: "PATCH",
    })
      .then((response) => response.json())
      .then((jsonResponse) => setSubject(jsonResponse));
    setSuccess(true);
    setFailure(false);
    setNameError(false);
    setChanged(false);
  };
  return (
    <div className="mx-3">
      <h2 className="my-5">Redaguoti dalyką</h2>
      
      
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
              <label htmlFor="edit-module-number-with-error">Pavadinimas *</label>
            </div>
            <div className="col-md-8 mb-2 mb-md-0">
        <TextField
          error={!!nameError}
          onChange={(e) => updateProperty("name", e)}
          value={subject.name}
          id="create-subject-number-with-error"
          label=""
          helperText="Pavadinimas negali būti tuščias"
          className="form-control mb-3"
          size="small"
          disabled={subject.deleted}
          InputLabelProps={{ shrink: true }}
          required
        />
        </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-name-with-error">Aprašymas</label>
            </div>
            <div className="col-md-8 mb-2">
        <TextField
          onChange={(e) => updateProperty("description", e)}
          value={subject.description}
          id="create-subject-number-with-error"
          label=""
          helperText="Neprivalomas"
          className="form-control mb-3"
          size="small"
          disabled={subject.deleted}
          InputLabelProps={{ shrink: true }}

        />
        </div>
          </div>
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-name-with-error">Modulis</label>
            </div>
            <div className="col-md-8 mb-2">
      <select
            value={selectedModule}
            onChange={(e) => setSelectedModule(e.target.value)}
            disabled={subject.deleted}
            className="form-control mb-3">
              <option value=''>{subject.module?.name}</option>
            {
                modules.map(
                    (mod) =>
                    (<option key={mod.id} 
                        value={mod.id}
                        disabled={mod.deleted}>{mod.name}</option>)
                )
            }
        </select>
        </div>
          </div>

      
          <div className="row">
            <div className="col-md-4 mb-2 mb-md-0 fw-bold">
              <label htmlFor="edit-module-name-with-error">Kabinetai</label>
            </div>
            <div className="col-md-8 mb-2">
        <div className="d-grid gap-6 d-md-block">
          {subject.rooms?.map((room) => (
          <button
            type="submit"
            className="btn btn-light"
            disabled={subject.deleted}
            value={room.name}
            onClick={(e) => deleteRoom(e.target.value)}
            key={room.name} id={room.name}
            >{room.name}</button>
        ))}</div>
<div>
        
        <select
          value={selectedRoom}
          onChange={(e) => setSelectedRoom(e.target.value)}
          className="form-control mb-3"
          disabled={subject.deleted}>
          <option value=''>---</option>
          {
            rooms.map(
              (room) =>
              (<option key={room.id} 
                  value={room.id} 
                  disabled={room.deleted}>{room.id}</option>)
          )
          }
        </select>
        </div>
        </div>
          </div>
          <div className="row mb-md-4">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">Būsena</div>
          <div className="col-md-8 mb-2 mb-md-0">
            {subject.deleted ? "Dalykas ištrintas" : "Aktyvus"}
          </div>
        </div>
        <div className="row mb-md-4">
          <div className="col-md-4 mb-2 mb-md-0 fw-bold">
      Paskutinį kartą modifikuotas
      </div>
      <div className="col-md-8 mb-2 mb-md-0">{subject.last_Updated}</div>
      </div>
      
        </form>
        </div>
        <div>
          
        <button type="submit" 
        className="btn btn-primary me-2" 
        onClick={editsubject}
        disabled={!changed}
        >
          
          Redaguoti
        </button>
        {subject.deleted ? (
            <button
              className="btn btn-secondary me-2"
              onClick={handleRestore}
            >
              Atstatyti
            </button>
          ) : (
            <button
              className="btn btn-danger me-2"
              onClick={handleDelete}
              
            >
              Ištrinti
            </button>
          )}
          </div>
      
    </div>
  );
}

export default EditSubjectPage;
