import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";

export default function EditTeacher() {
  let navigate = useNavigate();

  const { id } = useParams();

  const [teacher, setTeacher] = useState({
    name: "",
    lastname: "",
    email: "",
  });

  const { name, lastname, email } = teacher;

  const onInputChange = (e) => {
    setTeacher({ ...teacher, [e.target.name]: e.target.value });
  };

  useEffect(() => {
    loadTeacher();
  }, []);

  const onSubmit = async (e) => {
    e.preventDefault();
    await axios.put(`http://localhost:8080/teacher/${id}`, teacher);
    navigate("/");
  };

  const loadTeacher = async () => {
    const result = await axios.get(`http://localhost:8080/teacher/${id}`);
    setTeacher(result.data);
  };

  return (
    <div className="container">
      <div className="row">
        <div className="col-md-6 offset-md-3 border rounded p-4 mt-2 shadow">
          <h2 className="text-center m-4">Edit Teacher</h2>

          <form onSubmit={(e) => onSubmit(e)}>
            <div className="mb-3">
              <label htmlFor="Name" className="form-label">
                Name
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your name"
                name="name"
                value={name}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Lastname" className="form-label">
                Lastname
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your username"
                name="lastname"
                value={lastname}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <div className="mb-3">
              <label htmlFor="Email" className="form-label">
                E-mail
              </label>
              <input
                type={"text"}
                className="form-control"
                placeholder="Enter your e-mail address"
                name="email"
                value={email}
                onChange={(e) => onInputChange(e)}
              />
            </div>
            <button type="submit" className="btn btn-outline-primary">
              Patvirtinti
            </button>
            <Link className="btn btn-outline-danger mx-2" to="/">
              Atsaukti
            </Link>
          </form>
        </div>
      </div>
    </div>
  );
}