Here's a `README.md` file for your plugin with the MIT license:

```markdown
# TownyPermissions

TownyPermissions is a Minecraft plugin that integrates Towny and LuckPerms to manage permissions based on town membership. This plugin automatically assigns or removes permissions for players based on the town they belong to, ensuring that permissions are kept up-to-date without manual intervention.

## Features

- Automatically updates player permissions based on their town membership.
- Supports adding and removing permissions for towns.
- Optimized to minimize server load using event-based checks.
- Utilizes LuckPerms groups to manage permissions efficiently.

## Installation

1. Download the latest release of TownyPermissoins.
2. Place the `TownyPermissions.jar` file in your server's `plugins` directory.
3. Restart your server to load the plugin.

## Configuration

This plugin does not require any configuration files. Permissions are managed through commands.

## Commands

- `/tperm <Town> <add|remove> <permission>` - Adds or removes a permission for a specific town.

## Usage

To add a permission for a town:
```
/tperm <Town> add <permission>
```

To remove a permission from a town:
```
/tperm <Town> remove <permission>
```

### Example

To allow players in the town "Odessa" to use the fly command:
```
/tperm Odessa add cmi.command.fly
```

To remove the fly command permission from the town "Odessa":
```
/tperm Odessa remove cmi.command.fly
```

## Building from Source

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/TownyPermissions.git
    ```
2. Navigate to the project directory:
    ```sh
    cd TownyPermissions
    ```
3. Build the project using Maven:
    ```sh
    mvn clean package
    ```
4. The compiled `.jar` file will be located in the `target` directory.

## License

MIT License

```
MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Contributing

1. Fork the repository.
2. Create a new branch for your feature or bugfix.
3. Commit your changes.
4. Push to the branch.
5. Create a pull request.

## Issues

If you encounter any issues or have any suggestions, please open an issue on the [GitHub repository](https://github.com/yourusername/TownyPermissions/issues).

---

This plugin requires Towny and LuckPerms to be installed on the server. Make sure both plugins are up-to-date to avoid compatibility issues.
```

Make sure to replace `yourusername` with your actual GitHub username or the correct repository URL.
