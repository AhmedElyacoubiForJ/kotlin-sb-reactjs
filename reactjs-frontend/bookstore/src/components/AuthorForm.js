import React, { useState } from 'react';
import authorService from '../services/authorService';
import 'bootstrap/dist/css/bootstrap.min.css';

const AuthorForm = () => {
  const [author, setAuthor] = useState({
    name: '',
    age: null,
    description: '',
    image: ''
  });

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    setAuthor({ ...author, [name]: value });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const createdAuthor = await authorService.createAuthor(author);
      console.log('Author created:', createdAuthor);
      // Reset the form or perform other actions
    } catch (error) {
      console.error('Failed to create author:', error.message);
      // Handle error
    }
  };

  return (
    <form onSubmit={handleSubmit} className="container">
      <div className="form-group">
        <label htmlFor="name">Name:</label>
        <input
          type="text"
          className="form-control"
          id="name"
          name="name"
          value={author.name}
          onChange={handleInputChange}
        />
      </div>
      <div className="form-group">
        <label htmlFor="age">Age:</label>
        <input
          type="number"
          className="form-control"
          id="age"
          name="age"
          value={author.age}
          onChange={handleInputChange}
        />
      </div>
      <div className="form-group">
        <label htmlFor="description">Description:</label>
        <input
          type="text"
          className="form-control"
          id="description"
          name="description"
          value={author.description}
          onChange={handleInputChange}
        />
      </div>
      <div className="form-group">
        <label htmlFor="image">Image:</label>
        <input
          type="text"
          className="form-control"
          id="image"
          name="image"
          value={author.image}
          onChange={handleInputChange}
        />
      </div>
      {/* Add other form fields as needed */}
      <button type="submit" className="btn btn-primary">Create Author</button>
    </form>
  );
};

export default AuthorForm;