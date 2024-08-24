import React, { useState } from 'react';
import authorService from '../services/authorService';

const AuthorForm = () => {
  const [author, setAuthor] = useState({
    name: '',
    age: 0,
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
    <form onSubmit={handleSubmit}>
      <div>
        <label>Name:</label>
        <input
          type="text"
          name="name"
          value={author.name}
          onChange={handleInputChange}
        />
      </div>
      <div>
        <label>Age:</label>
        <input
          type="number"
          name="age"
          value={author.age}
          onChange={handleInputChange}
        />
      </div>
      <div>
        <label>Description:</label>
        <input
          type="text"
          name="description"
          value={author.description}
          onChange={handleInputChange}
        />
      </div>
      <div>
        <label>Image:</label>
        <input
          type="text"
          name="image"
          value={author.image}
          onChange={handleInputChange}
        />
      </div>
      {/* Add other form fields as needed */}
      <button type="submit">Create Author</button>
    </form>
  );
};

export default AuthorForm;
